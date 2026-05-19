<template>
  <div class="f-card data-feed-container">
    <div class="card-head" v-if="title || subtitle">
      <div class="title-group">
        <h3 class="f-card-title" v-if="title">{{ title }}</h3>
        <p class="f-card-subtitle" v-if="subtitle">{{ subtitle }}</p>
      </div>
    </div>

    <div class="feed-body">

      <!-- PROYECTOS QUE VENCEN EN ≤30 DÍAS -->
      <div class="attention-section">
        <h4 class="mini-title">Proyectos por vencer</h4>
        <div v-if="expiringProjects.length > 0" class="alert-list">
          <div
            v-for="p in expiringProjects"
            :key="p.idProject"
            class="alert-item alert-item--warn"
            @click="router.push('/projects')"
          >
            <div class="alert-dot alert-dot--warn"></div>
            <div class="alert-text">
              <span class="alert-name">{{ p.name.length > 28 ? p.name.slice(0, 28) + '…' : p.name }}</span>
              <span class="alert-meta warn">Vence en {{ p.daysLeft }} día{{ p.daysLeft !== 1 ? 's' : '' }}</span>
            </div>
          </div>
        </div>
        <p v-else class="empty-alert">Sin proyectos próximos a vencer</p>
      </div>

      <div class="divider"></div>

      <!-- EMPLEADOS SIN PROYECTO ACTIVO -->
      <div class="attention-section">
        <h4 class="mini-title">Empleados sin proyecto</h4>
        <div v-if="unassignedList.length > 0" class="alert-list">
          <div
            v-for="e in unassignedList"
            :key="e.idEmployee"
            class="alert-item"
            @click="router.push('/employees')"
          >
            <div class="alert-dot"></div>
            <div class="alert-text">
              <span class="alert-name">{{ e.name }}</span>
              <span class="alert-meta">Sin asignación activa</span>
            </div>
          </div>
        </div>
        <p v-else class="empty-alert">Toda la plantilla asignada</p>
      </div>

    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()

defineProps({
  title:             String,
  subtitle:          String,
  expiringProjects:  { type: Array, default: () => [] },
  unassignedList:    { type: Array, default: () => [] }
})
</script>

<style scoped>
.data-feed-container { display: flex; flex-direction: column; }

.feed-body { display: flex; flex-direction: column; gap: 20px; }

.attention-section { display: flex; flex-direction: column; gap: 12px; }

.mini-title {
  font-size: 13px;
  font-weight: 600;
  color: #1E293B;
  text-transform: none;
  letter-spacing: normal;
}

.alert-list { display: flex; flex-direction: column; gap: 10px; }

.alert-item {
  display: flex; align-items: center; gap: 12px;
  padding: 10px 12px; border-radius: 12px;
  cursor: pointer; transition: background 0.2s;
}
.alert-item:hover { background: rgba(49, 46, 129, 0.04); }
.alert-item--warn:hover { background: rgba(245, 158, 11, 0.05); }

.alert-dot {
  width: 8px; height: 8px; border-radius: 50%;
  background: #312E81; flex-shrink: 0;
}
.alert-dot--warn { background: #F59E0B; }

.alert-text { display: flex; flex-direction: column; gap: 2px; min-width: 0; }
.alert-name { font-size: 13px; font-weight: 600; color: #1E293B; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
.alert-meta { font-size: 11px; font-weight: 500; color: #94A3B8; }
.alert-meta.warn { color: #F59E0B; }

.empty-alert { font-size: 12px; font-weight: 500; color: #CBD5E1; font-style: italic; }

.divider { height: 1px; background: rgba(15, 23, 42, 0.05); }
</style>
