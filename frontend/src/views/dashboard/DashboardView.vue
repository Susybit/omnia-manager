<template>
  <div class="f-page-view">
    <div class="aura aura-dash"></div>

    <header class="f-header">
      <div class="f-header-content">
        <h1 class="text-h4 font-weight-bold">{{ greeting }}, {{ userName }}</h1>
      </div>

      <div class="f-header-actions">
        <button class="btn-f-base btn-f-outline" @click="$router.push('/employees')">
          <UserPlus class="icon-sm mr-2" />
          Alta Empleados
        </button>
        <button class="btn-f-base btn-f-primary" @click="$router.push('/projects')">
          <Plus class="icon-sm mr-2" />
          Alta Proyectos
        </button>
      </div>
    </header>

    <!-- MALLA DE INDICADORES (KPIs) -->
    <section class="f-kpi-grid animate-in">
      <StatCard
        v-for="kpi in kpiMetrics"
        :key="kpi.label"
        v-bind="kpi"
      />
    </section>

    <!-- BENTO GRID DE VISUALIZACIÓN -->
    <section class="f-bento-grid">
      <ChartArea
        title="Carga de equipos"
        subtitle="Empleados asignados · top 5 proyectos"
        :data="barData"
        :labels="chartLabels"
        class="animate-in delay-1"
      />

      <DataFeed
        title="Requieren atención"
        :expiringProjects="expiringProjects"
        :unassignedList="unassignedList"
        class="animate-in delay-2"
      />
    </section>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { fetchDashboardData } from '@/services/dashboardService'
import { UserPlus, Users, Plus, Briefcase, Activity, UserX } from 'lucide-vue-next'
import { useAuthStore } from '@/stores/auth'
import { toast } from '@/services/toastService'
import StatCard from '@/components/dashboard/StatCard.vue'
import ChartArea from '@/components/dashboard/ChartArea.vue'
import DataFeed from '@/components/dashboard/DataFeed.vue'

const authStore = useAuthStore()
const userName = computed(() => authStore.user?.name?.split(' ')[0] || 'Admin')

const barData        = ref([])
const chartLabels    = ref([])
const expiringProjects = ref([])
const unassignedList   = ref([])

const statistics = reactive({ employees: 0, projects: 0, occupancy: 0, unassigned: 0 })

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 12) return 'Buenos días'
  if (h < 20) return 'Buenas tardes'
  return 'Buenas noches'
})

const kpiMetrics = computed(() => [
  {
    label: 'Empleados activos',
    value: statistics.employees,
    accent: '#312E81',
    icon: Users,
    trendLabel: 'en plantilla activa'
  },
  {
    label: 'Proyectos activos',
    value: statistics.projects,
    accent: '#7C3AED',
    icon: Briefcase,
    trendLabel: 'en curso'
  },
  {
    label: 'Ocupación',
    value: statistics.occupancy,
    suffix: '%',
    accent: '#4F46E5',
    icon: Activity,
    trendLabel: 'empleados asignados'
  },
  {
    label: 'Sin asignar',
    value: statistics.unassigned,
    accent: statistics.unassigned > 0 ? '#F59E0B' : '#312E81',
    icon: UserX,
    trendLabel: statistics.unassigned > 0 ? 'requieren asignación' : 'plantilla cubierta'
  }
])

/**
 * Carga empleados, proyectos y asignaciones en paralelo y deriva todos los indicadores.
 * Las asignaciones se filtran a proyectos activos antes de cualquier cálculo para evitar
 * contar empleados "asignados" a proyectos ya cerrados.
 */
const sync = async () => {
  try {
    const { employees, projects, assignments } = await fetchDashboardData()

    const activeE          = employees.filter(e => !e.terminationDate)
    const activeP          = projects.filter(p => !p.terminationDate)
    const activeProjectIds = new Set(activeP.map(p => p.idProject))
    const activeAssign     = assignments.filter(a => activeProjectIds.has(a.idProject))
    const assignedEmpIds   = new Set(activeAssign.map(a => a.idEmployee))

    statistics.employees  = activeE.length
    statistics.projects   = activeP.length
    statistics.occupancy  = activeE.length > 0
      ? Math.round((assignedEmpIds.size / activeE.length) * 100)
      : 0
    statistics.unassigned = activeE.filter(e => !assignedEmpIds.has(e.idEmployee)).length

    // Carga por proyecto — top 5 activos con más empleados asignados
    const loadMap = {}
    activeAssign.forEach(a => { loadMap[a.idProject] = (loadMap[a.idProject] || 0) + 1 })
    const top5 = activeP
      .map(p => ({ name: p.description, count: loadMap[p.idProject] || 0 }))
      .filter(p => p.count > 0)
      .sort((a, b) => b.count - a.count)
      .slice(0, 5)
    chartLabels.value = top5.map(p => p.name.length > 22 ? p.name.slice(0, 22) + '…' : p.name)
    barData.value     = top5.map(p => p.count)

    // Proyectos que vencen en 30 días o menos
    const today = new Date()
    expiringProjects.value = activeP
      .filter(p => {
        if (!p.endDate) return false
        const days = Math.ceil((new Date(p.endDate) - today) / 86400000)
        return days >= 0 && days <= 30
      })
      .map(p => ({
        idProject: p.idProject,
        name: p.description,
        daysLeft: Math.ceil((new Date(p.endDate) - today) / 86400000)
      }))
      .sort((a, b) => a.daysLeft - b.daysLeft)
      .slice(0, 4)

    // Primeros 4 empleados activos sin ningún proyecto activo
    unassignedList.value = activeE
      .filter(e => !assignedEmpIds.has(e.idEmployee))
      .slice(0, 4)
      .map(e => ({ idEmployee: e.idEmployee, name: `${e.firstName} ${e.lastName}` }))

  } catch (e) {
    console.error('[Dashboard] Error sync:', e)
    toast.error('Error al cargar el panel de control')
  }
}

onMounted(sync)
</script>

<style scoped>
.aura { position: fixed; border-radius: 50%; filter: blur(120px); z-index: -1; pointer-events: none; }
.aura-dash { width: 500px; height: 500px; background: radial-gradient(circle, #312E81, transparent); bottom: -100px; left: -100px; opacity: 0.07; }

.f-kpi-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 24px; margin-bottom: 32px; }
.f-bento-grid { display: grid; grid-template-columns: 1fr; gap: 24px; align-items: start; }

@media (min-width: 1200px) {
  .f-bento-grid { grid-template-columns: 1.8fr 1.2fr; }
}

.icon-sm { width: 14px; height: 14px; stroke-width: 2px; }
</style>
