<template>
  <Transition name="toast-slide">
    <div v-if="modelValue" class="toast-positioner">
      <div class="toast-crystal" :class="type">
        <component :is="currentIcon" class="toast-icon" />
        <span class="toast-text">{{ message }}</span>
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { computed, watch } from 'vue'
import { CheckCircle2, XCircle, Info } from 'lucide-vue-next'

const props = defineProps({
  modelValue: Boolean,
  message:    String,
  type:       { type: String, default: 'success' },
  duration:   { type: Number, default: 3500 }
})

const emit = defineEmits(['update:modelValue'])

const currentIcon = computed(() => {
  if (props.type === 'error') return XCircle
  if (props.type === 'info')  return Info
  return CheckCircle2
})

watch(() => props.modelValue, (val) => {
  if (val) setTimeout(() => emit('update:modelValue', false), props.duration)
})
</script>

<style scoped>
.toast-positioner {
  position: fixed;
  top: 24px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 9999;
  pointer-events: none;
  max-width: min(500px, calc(100vw - 48px));
  width: max-content;
}

/* Mismo lenguaje visual que CrystalCard */
.toast-crystal {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 12px 20px;
  border-radius: 20px;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(32px);
  -webkit-backdrop-filter: blur(32px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.06);
}

.toast-icon {
  width: 16px;
  height: 16px;
  stroke-width: 2px;
  flex-shrink: 0;
  margin-top: 1px;
}

.toast-crystal.success .toast-icon { color: #312E81; }
.toast-crystal.error   .toast-icon { color: #EF4444; }
.toast-crystal.info    .toast-icon { color: #64748B; }

.toast-text {
  font-size: 13px;
  font-weight: 500;
  color: #1E293B;
  letter-spacing: -0.01em;
  line-height: 1.5;
  word-break: break-word;
}

.toast-slide-enter-active { transition: all 0.4s cubic-bezier(0.2, 1, 0.3, 1); }
.toast-slide-leave-active { transition: all 0.25s ease; }
.toast-slide-enter-from   { opacity: 0; transform: translateX(-50%) translateY(-10px); }
.toast-slide-leave-to     { opacity: 0; transform: translateX(-50%) translateY(-6px); }
</style>
