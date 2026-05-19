<template>
  <div class="f-card">
    <div class="card-head">
      <div class="title-group">
        <h3 class="f-card-title">{{ title }}</h3>
        <p class="f-card-subtitle">{{ subtitle }}</p>
      </div>
    </div>

    <div class="chart-viewport" v-if="hasData">
      <Bar :data="chartData" :options="chartOptions" />
    </div>

    <div v-else class="empty-state">
      <div class="empty-icon-box">
        <BarChart3 class="empty-icon" />
      </div>
      <p class="empty-text">Sin asignaciones activas</p>
      <p class="empty-sub">Asigna empleados a proyectos para ver la carga.</p>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { Bar } from 'vue-chartjs'
import { BarChart3 } from 'lucide-vue-next'
import 'chart.js/auto'

const props = defineProps({
  title:    { type: String, default: 'Carga de equipos' },
  subtitle: { type: String, default: '' },
  data:     { type: Array,  default: () => [] },
  labels:   { type: Array,  default: () => [] }
})

const hasData = computed(() => props.data.length > 0 && props.data.some(v => Number(v) > 0))

/** Opacidad proporcional al valor — la barra más grande siempre al 90%, la más pequeña al 20%. */
const chartData = computed(() => {
  const max = Math.max(...props.data, 1)
  return {
    labels: props.labels,
    datasets: [{
      label: 'Empleados',
      data: props.data.map(v => Number(v)),
      backgroundColor: props.data.map(v => `rgba(49, 46, 129, ${0.2 + (v / max) * 0.7})`),
      borderWidth: 0,
      borderRadius: 8
    }]
  }
})

const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  indexAxis: 'y',
  plugins: {
    legend: { display: false },
    tooltip: { callbacks: { label: ctx => ` ${ctx.raw} empleado${ctx.raw !== 1 ? 's' : ''}` } }
  },
  scales: {
    x: {
      beginAtZero: true,
      grid: { color: 'rgba(0,0,0,0.03)' },
      ticks: { color: '#94A3B8', font: { size: 11 }, stepSize: 1 }
    },
    y: {
      grid: { display: false },
      ticks: { color: '#64748B', font: { size: 11 } }
    }
  }
}
</script>

<style scoped>
.card-head { margin-bottom: 24px; }
.chart-viewport { height: 280px; }

.empty-state {
  height: 280px;
  display: flex; flex-direction: column;
  align-items: center; justify-content: center; text-align: center;
}
.empty-icon-box {
  width: 60px; height: 60px;
  background: #F8FAFC; border: 1px solid #E2E8F0; border-radius: 20px;
  display: flex; align-items: center; justify-content: center; margin-bottom: 16px;
}
.empty-icon { color: #64748B; width: 24px; height: 24px; }
.empty-text { font-weight: 700; color: #1E293B; font-size: 14px; }
.empty-sub  { font-size: 12px; color: #64748B; margin-top: 4px; }
</style>
