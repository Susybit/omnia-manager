<template>
  <div class="f-page-view">
    <div class="aura aura-analytics"></div>

    <header class="f-header">
      <div class="f-header-content">
        <h1 class="text-h4 font-weight-bold">Analítica</h1>
        <p class="f-card-subtitle">Métricas en tiempo real · {{ today }}</p>
      </div>
    </header>

    <div v-if="loading" class="loading-state">
      <v-progress-circular indeterminate color="#312E81" size="48" width="3" />
    </div>

    <template v-else>

      <!-- ── HERO METRICS ────────────────────────────────────────────── -->
      <section class="hero-grid animate-in">

        <div class="hero-card">
          <Users class="hero-icon" style="color:#312E81" />
          <div class="hero-number" style="color:#312E81">{{ analytics.employees.active }}</div>
          <div class="hero-label">Empleados activos</div>
          <div class="hero-sub">{{ analytics.employees.total }} en el histórico</div>
        </div>

        <div class="hero-card">
          <Briefcase class="hero-icon" style="color:#7C3AED" />
          <div class="hero-number" style="color:#7C3AED">{{ analytics.projects.active }}</div>
          <div class="hero-label">Proyectos activos</div>
          <div class="hero-sub">{{ avgDurationMonths }} meses de duración media</div>
        </div>

        <div class="hero-card" :class="{ 'hero-card--warn': analytics.projects.uncoveredProjects > 0 }">
          <AlertTriangle class="hero-icon" :style="{ color: analytics.projects.uncoveredProjects > 0 ? '#F59E0B' : '#312E81' }" />
          <div class="hero-number" :style="{ color: analytics.projects.uncoveredProjects > 0 ? '#F59E0B' : '#312E81' }">
            {{ analytics.projects.uncoveredProjects }}
          </div>
          <div class="hero-label">{{ analytics.projects.uncoveredProjects > 0 ? 'Sin equipo' : 'Cobertura total' }}</div>
          <div class="hero-sub">{{ analytics.projects.uncoveredProjects > 0 ? 'proyectos sin asignar' : 'todos los proyectos cubiertos' }}</div>
        </div>

        <div class="hero-card">
          <GraduationCap class="hero-icon" style="color:#7C3AED" />
          <div class="hero-number" style="color:#7C3AED">{{ analytics.employees.universityPct }}<span class="hero-suffix">%</span></div>
          <div class="hero-label">Titulados</div>
          <div class="hero-sub">de la plantilla activa</div>
        </div>

        <div class="hero-card" :class="{ 'hero-card--alert': analytics.employees.multiProjectEmployees > 0 }">
          <Network class="hero-icon" style="color:#F59E0B" />
          <div class="hero-number" style="color:#F59E0B">{{ analytics.employees.multiProjectEmployees }}</div>
          <div class="hero-label">Multi-proyecto</div>
          <div class="hero-sub">empleados en 2 o más proyectos</div>
        </div>

      </section>

      <!-- ── FILA 1: CARGA + ROTACIÓN ────────────────────────────────── -->
      <section class="charts-grid animate-in delay-1">

        <div class="f-card">
          <div class="card-head">
            <div>
              <h3 class="f-card-title">Carga de proyectos</h3>
              <p class="f-card-subtitle">Empleados asignados · top 8</p>
            </div>
            <span class="card-badge">{{ analytics.projectLoad.length }} proyectos</span>
          </div>
          <div class="chart-viewport">
            <Bar v-if="projectLoadData" :data="projectLoadData" :options="barHOptions" />
            <EmptyChart v-else message="Sin asignaciones registradas" />
          </div>
        </div>

        <div class="f-card">
          <div class="card-head">
            <div>
              <h3 class="f-card-title">Rotación de plantilla</h3>
              <p class="f-card-subtitle">Activos vs bajas históricas</p>
            </div>
            <span class="card-badge">{{ turnoverPct }}% rotación</span>
          </div>
          <div class="donut-wrapper">
            <div class="donut-canvas">
              <Doughnut :data="turnoverData" :options="donutOptions" />
            </div>
            <div class="donut-center">
              <span class="donut-pct">{{ turnoverPct }}%</span>
              <span class="donut-clabel">rotación</span>
            </div>
          </div>
          <div class="chart-legend">
            <div class="legend-item"><span class="legend-dot" style="background:#312E81"></span>Activos {{ analytics.employees.active }}</div>
            <div class="legend-item"><span class="legend-dot" style="background:#818CF8"></span>Bajas {{ analytics.employees.inactive }}</div>
          </div>
        </div>

      </section>

      <!-- ── FILA 2: SEDES + FORMACIÓN ───────────────────────────────── -->
      <section class="charts-grid animate-in delay-2">

        <div class="f-card">
          <div class="card-head">
            <div>
              <h3 class="f-card-title">Proyectos por sede</h3>
              <p class="f-card-subtitle">Distribución geográfica activa</p>
            </div>
            <span class="card-badge">{{ analytics.locationBreakdown.length }} sedes</span>
          </div>
          <div class="chart-viewport">
            <Bar v-if="locationData" :data="locationData" :options="barOptions" />
            <EmptyChart v-else message="Sin proyectos activos" />
          </div>
        </div>

        <div class="f-card">
          <div class="card-head">
            <div>
              <h3 class="f-card-title">Formación universitaria</h3>
              <p class="f-card-subtitle">Plantilla activa</p>
            </div>
          </div>
          <div class="donut-wrapper">
            <div class="donut-canvas">
              <Doughnut :data="universityData" :options="donutOptions" />
            </div>
            <div class="donut-center">
              <span class="donut-pct">{{ analytics.employees.universityPct }}%</span>
              <span class="donut-clabel">Con título</span>
            </div>
          </div>
          <div class="chart-legend">
            <div class="legend-item"><span class="legend-dot" style="background:#7C3AED"></span>Con título {{ analytics.employees.universityPct }}%</div>
            <div class="legend-item"><span class="legend-dot" style="background:#A78BFA"></span>Sin título {{ 100 - analytics.employees.universityPct }}%</div>
          </div>
        </div>

      </section>

      <!-- ── EVOLUCIÓN DE LA PLANTILLA ───────────────────────────────── -->
      <section class="f-card evolution-card animate-in delay-3">
        <div class="card-head">
          <div>
            <h3 class="f-card-title">Evolución de la plantilla</h3>
            <p class="f-card-subtitle">Altas por año · histórico completo</p>
          </div>
          <span v-if="analytics.hiresPerYear.length" class="card-badge">
            {{ analytics.hiresPerYear[0].name }} — {{ analytics.hiresPerYear[analytics.hiresPerYear.length - 1].name }}
          </span>
        </div>
        <div class="chart-viewport">
          <Line v-if="hiresLineData" :data="hiresLineData" :options="lineOptions" />
          <EmptyChart v-else message="Sin datos históricos suficientes" />
        </div>
      </section>

      <!-- ── DISTRIBUCIONES: ANTIGÜEDAD + EDAD ──────────────────────── -->
      <section class="distrib-grid animate-in delay-3">

        <div class="f-card">
          <div class="card-head">
            <div>
              <h3 class="f-card-title">Distribución de antigüedad</h3>
              <p class="f-card-subtitle">Tramos por años en la empresa</p>
            </div>
            <span class="card-badge">{{ analytics.employees.active }} empleados</span>
          </div>
          <div class="tenure-bars">
            <div v-for="(bucket, idx) in analytics.tenureBreakdown" :key="bucket.name" class="tenure-row">
              <div class="tenure-label">{{ bucket.name }}</div>
              <div class="tenure-track">
                <div class="tenure-fill" :style="{ width: barWidth(bucket.count, analytics.tenureBreakdown) + '%', background: TENURE_COLORS[idx] }"></div>
              </div>
              <div class="tenure-count">{{ bucket.count }} <span class="tenure-unit">personas</span></div>
            </div>
          </div>
        </div>

        <div class="f-card">
          <div class="card-head">
            <div>
              <h3 class="f-card-title">Distribución de edad</h3>
              <p class="f-card-subtitle">Tramos generacionales · plantilla activa</p>
            </div>
            <span class="card-badge">edad media {{ analytics.employees.avgAge }} años</span>
          </div>
          <div class="tenure-bars">
            <div v-for="(bucket, idx) in analytics.ageBreakdown" :key="bucket.name" class="tenure-row">
              <div class="tenure-label">{{ bucket.name }}</div>
              <div class="tenure-track">
                <div class="tenure-fill" :style="{ width: barWidth(bucket.count, analytics.ageBreakdown) + '%', background: AGE_COLORS[idx] }"></div>
              </div>
              <div class="tenure-count">{{ bucket.count }} <span class="tenure-unit">personas</span></div>
            </div>
          </div>
        </div>

      </section>

    </template>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Bar, Doughnut, Line } from 'vue-chartjs'
import { Users, Briefcase, GraduationCap, AlertTriangle, Network } from 'lucide-vue-next'
import 'chart.js/auto'
import { fetchAnalytics } from '@/services/analyticsService'
import { toast } from '@/services/toastService'

// ── Constantes de diseño ─────────────────────────────────────────────────────

/** Escala secuencial índigo para la antigüedad — de medio a intenso, sin tonos lavados. */
const TENURE_COLORS = ['#818CF8', '#6366F1', '#4F46E5', '#4338CA', '#312E81']

/** Escala secuencial violeta para la edad — distinguible de índigo, igualmente saturada. */
const AGE_COLORS = ['#A78BFA', '#8B5CF6', '#7C3AED', '#6D28D9', '#5B21B6']

// ── Estado ───────────────────────────────────────────────────────────────────

const analytics = ref(null)
const loading   = ref(true)

const today = new Intl.DateTimeFormat('es-ES', { day: 'numeric', month: 'long', year: 'numeric' }).format(new Date())

// ── Derivados simples ────────────────────────────────────────────────────────

const turnoverPct = computed(() => {
  const emp = analytics.value?.employees
  if (!emp || emp.total === 0) return 0
  return Math.round((emp.inactive / emp.total) * 100)
})

/** Duración media de proyectos expresada en meses enteros. */
const avgDurationMonths = computed(() =>
  Math.round((analytics.value?.projects?.avgProjectDurationDays ?? 0) / 30)
)

// ── Componente vacío inline ──────────────────────────────────────────────────

const EmptyChart = {
  props: { message: String },
  template: `
    <div style="height:100%;display:flex;flex-direction:column;align-items:center;justify-content:center;gap:12px;">
      <div style="width:48px;height:48px;background:#F8FAFC;border:1px solid #E2E8F0;border-radius:16px;display:flex;align-items:center;justify-content:center;">
        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="#94A3B8" stroke-width="2">
          <rect x="3" y="3" width="18" height="18" rx="2"/><path d="M3 9h18M9 21V9"/>
        </svg>
      </div>
      <p style="font-size:13px;font-weight:600;color:#94A3B8;">{{ message }}</p>
    </div>
  `
}

// ── Datos de gráficos ────────────────────────────────────────────────────────

/** Barras monocromáticas — opacidad proporcional al valor, la más alta destaca sola. */
const locationData = computed(() => {
  const items = analytics.value?.locationBreakdown
  if (!items?.length) return null
  const max = Math.max(...items.map(i => i.count))
  return {
    labels: items.map(i => i.name),
    datasets: [{
      label: 'Proyectos',
      data: items.map(i => i.count),
      backgroundColor: items.map(i => `rgba(49, 46, 129, ${0.15 + (i.count / max) * 0.75})`),
      borderWidth: 0,
      borderRadius: 10
    }]
  }
})

const projectLoadData = computed(() => {
  const items = analytics.value?.projectLoad
  if (!items?.length) return null
  const max = Math.max(...items.map(i => i.count))
  return {
    labels: items.map(i => i.name.length > 24 ? i.name.slice(0, 24) + '…' : i.name),
    datasets: [{
      label: 'Empleados',
      data: items.map(i => i.count),
      backgroundColor: items.map(i => `rgba(49, 46, 129, ${0.15 + (i.count / max) * 0.75})`),
      borderWidth: 0,
      borderRadius: 10
    }]
  }
})

const turnoverData = computed(() => {
  const emp = analytics.value?.employees
  if (!emp) return null
  return {
    labels: ['Activos', 'Bajas'],
    datasets: [{ data: [emp.active, emp.inactive], backgroundColor: ['#312E81', '#818CF8'], borderWidth: 0, hoverOffset: 6 }]
  }
})

const universityData = computed(() => {
  const emp = analytics.value?.employees
  if (!emp) return null
  return {
    labels: ['Con título', 'Sin título'],
    datasets: [{ data: [emp.universityPct, 100 - emp.universityPct], backgroundColor: ['#7C3AED', '#A78BFA'], borderWidth: 0, hoverOffset: 6 }]
  }
})

/** Línea de evolución — áreas rellenas con degradado índigo, tensión suavizada. */
const hiresLineData = computed(() => {
  const items = analytics.value?.hiresPerYear
  if (!items || items.length < 2) return null
  return {
    labels: items.map(i => i.name),
    datasets: [{
      label: 'Altas',
      data: items.map(i => i.count),
      borderColor: '#312E81',
      backgroundColor: 'rgba(49, 46, 129, 0.07)',
      fill: true,
      tension: 0.4,
      pointRadius: 5,
      pointBackgroundColor: '#FFF',
      pointBorderColor: '#312E81',
      pointBorderWidth: 2,
      borderWidth: 2.5
    }]
  }
})

// ── Opciones de gráficos ─────────────────────────────────────────────────────

const barOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { display: false }, tooltip: { callbacks: { label: ctx => ` ${ctx.raw} proyecto${ctx.raw !== 1 ? 's' : ''}` }}},
  scales: {
    x: { grid: { display: false }, ticks: { color: '#94A3B8', font: { size: 11 } } },
    y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.03)' }, ticks: { color: '#94A3B8', font: { size: 11 }, stepSize: 1 } }
  }
}

const barHOptions = {
  responsive: true,
  maintainAspectRatio: false,
  indexAxis: 'y',
  plugins: { legend: { display: false }, tooltip: { callbacks: { label: ctx => ` ${ctx.raw} empleado${ctx.raw !== 1 ? 's' : ''}` }}},
  scales: {
    x: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.03)' }, ticks: { color: '#94A3B8', font: { size: 11 }, stepSize: 1 } },
    y: { grid: { display: false }, ticks: { color: '#64748B', font: { size: 11 } } }
  }
}

const donutOptions = {
  responsive: true,
  maintainAspectRatio: false,
  cutout: '72%',
  plugins: { legend: { display: false }, tooltip: { callbacks: { label: ctx => ` ${ctx.raw}${typeof ctx.raw === 'number' && ctx.raw < 100 ? '' : ''}` }}}
}

const lineOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: { legend: { display: false }, tooltip: { callbacks: { label: ctx => ` ${ctx.raw} alta${ctx.raw !== 1 ? 's' : ''}` }}},
  scales: {
    x: { grid: { display: false }, ticks: { color: '#94A3B8', font: { size: 11 } } },
    y: { beginAtZero: true, grid: { color: 'rgba(0,0,0,0.03)' }, ticks: { color: '#94A3B8', font: { size: 11 }, stepSize: 1 } }
  }
}

// ── Utilidades ───────────────────────────────────────────────────────────────

/**
 * Calcula el % de ancho de una barra relativo al máximo del array.
 * Garantiza que siempre haya una barra al 100% para anclar visualmente la escala.
 */
function barWidth(count, buckets) {
  const max = Math.max(...buckets.map(b => b.count))
  return max === 0 ? 0 : Math.round((count / max) * 100)
}

// ── Carga de datos ───────────────────────────────────────────────────────────

onMounted(async () => {
  try {
    analytics.value = await fetchAnalytics()
  } catch {
    toast.error('No se pudieron cargar los datos de analítica')
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.aura-analytics {
  position: fixed;
  width: 500px; height: 500px;
  background: radial-gradient(circle, #312E81, transparent);
  top: -150px; right: -150px;
  border-radius: 50%;
  filter: blur(130px);
  z-index: -1; opacity: 0.08; pointer-events: none;
}

.loading-state {
  display: flex; justify-content: center; align-items: center; height: 300px;
}

/* ── HERO ─────────────────────────────────────────────────────────── */
.hero-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(175px, 1fr));
  gap: 16px;
  margin-bottom: 32px;
}

.hero-card {
  background: white;
  border-radius: 20px;
  border: 1px solid rgba(15, 23, 42, 0.05);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.03);
  padding: 24px 20px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.hero-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.06);
}

.hero-card--warn {
  border-color: rgba(245, 158, 11, 0.25);
  background: linear-gradient(135deg, white 80%, rgba(245, 158, 11, 0.04));
}

.hero-card--alert {
  border-color: rgba(245, 158, 11, 0.15);
}

.hero-icon { width: 20px; height: 20px; margin-bottom: 8px; stroke-width: 2px; }

.hero-number {
  font-family: 'Outfit', sans-serif;
  font-size: 40px;
  font-weight: 800;
  line-height: 1;
  letter-spacing: -0.04em;
}

.hero-suffix { font-size: 22px; font-weight: 700; letter-spacing: -0.02em; }
.hero-label  { font-size: 12px; font-weight: 700; color: #1E293B; margin-top: 6px; }
.hero-sub    { font-size: 11px; font-weight: 500; color: #94A3B8; }

/* ── GRIDS DE GRÁFICOS ────────────────────────────────────────────── */
.charts-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 24px;
  margin-bottom: 32px;
}

@media (min-width: 1200px) {
  .charts-grid { grid-template-columns: 1.8fr 1.2fr; }
}

.card-head {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.card-badge {
  font-size: 11px;
  font-weight: 700;
  color: #312E81;
  background: rgba(49, 46, 129, 0.08);
  border-radius: 8px;
  padding: 4px 10px;
  white-space: nowrap;
}

.chart-viewport { height: 260px; }

/* ── DONUTS ───────────────────────────────────────────────────────── */
.donut-wrapper {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 200px;
}

.donut-canvas { width: 100%; height: 100%; }

.donut-center {
  position: absolute;
  top: 50%; left: 50%;
  transform: translate(-50%, -50%);
  text-align: center;
  pointer-events: none;
}

.donut-pct {
  display: block;
  font-family: 'Outfit', sans-serif;
  font-size: 30px;
  font-weight: 800;
  color: #1E293B;
  letter-spacing: -0.04em;
  line-height: 1;
}

.donut-clabel {
  display: block;
  font-size: 11px;
  font-weight: 600;
  color: #94A3B8;
  margin-top: 4px;
}

.chart-legend {
  display: flex; gap: 20px; justify-content: center;
  margin-top: 16px; flex-wrap: wrap;
}

.legend-item {
  display: flex; align-items: center; gap: 8px;
  font-size: 12px; font-weight: 600; color: #475569;
}

.legend-dot { width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0; }

/* ── LÍNEA DE EVOLUCIÓN ───────────────────────────────────────────── */
.evolution-card {
  margin-bottom: 32px;
}

/* ── DISTRIBUCIONES ───────────────────────────────────────────────── */
.distrib-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 24px;
}

@media (min-width: 1000px) {
  .distrib-grid { grid-template-columns: 1fr 1fr; }
}

.tenure-bars { display: flex; flex-direction: column; gap: 16px; margin-top: 8px; }

.tenure-row {
  display: grid;
  grid-template-columns: 80px 1fr 100px;
  align-items: center;
  gap: 16px;
}

.tenure-label  { font-size: 12px; font-weight: 700; color: #475569; white-space: nowrap; }
.tenure-track  { height: 10px; background: #F1F5F9; border-radius: 99px; overflow: hidden; }
.tenure-fill   { height: 100%; border-radius: 99px; transition: width 0.8s cubic-bezier(0.2, 0.8, 0.2, 1); }
.tenure-count  { font-size: 13px; font-weight: 700; color: #1E293B; text-align: right; }
.tenure-unit   { font-weight: 500; color: #94A3B8; }

.delay-3 { animation-delay: 0.45s; }
</style>
