<template>
  <component :is="tag" class="crystal-card" :class="[customClass, { 'is-hoverable': hoverable }]">
    <slot></slot>
  </component>
</template>

<script setup>
defineProps({
  tag: {
    type: String,
    default: 'div'
  },
  customClass: {
    type: String,
    default: ''
  },
  hoverable: {
    type: Boolean,
    default: true
  }
})
</script>

<style scoped>
.crystal-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(32px);
  -webkit-backdrop-filter: blur(32px);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 24px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.02);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  z-index: 2;
}

.is-hoverable:hover {
  transform: translateY(-4px);
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.05);
  background: rgba(255, 255, 255, 0.95);
  border-color: rgba(255, 255, 255, 0.4);
}
</style>
