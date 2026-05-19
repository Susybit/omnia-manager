<template>
  <v-btn
    :class="['f-button', `v-${variant}`]"
    :variant="vuetifyVariant"
    :color="computedColor"
    :ripple="false"
    class="text-none"
    v-bind="$attrs"
  >
    <template v-if="$slots.prepend" v-slot:prepend>
      <slot name="prepend" />
    </template>
    
    <span class="f-button-text">
      <slot />
    </span>

    <template v-if="$slots.append" v-slot:append>
      <slot name="append" />
    </template>
  </v-btn>
</template>

<script setup>
import { computed } from 'vue';

const props = defineProps({
  // solid, outline, text, dashboard
  variant: {
    type: String,
    default: 'text'
  },
  color: {
    type: String,
    default: 'primary'
  }
});

const vuetifyVariant = computed(() => {
  switch (props.variant) {
    case 'solid': return 'flat';
    case 'dashboard': return 'flat';
    case 'outline': return 'outlined';
    case 'text': return 'text';
    default: return 'text';
  }
});

const computedColor = computed(() => {
  return props.color === 'primary' ? '#312E81' : props.color;
});
</script>

<style scoped>
.f-button {
  font-family: 'Inter', sans-serif;
  font-weight: 600;
  letter-spacing: -0.01em;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

/* Variante DASHBOARD (Estilo Login + Plus Radius) */
.v-dashboard {
  border-radius: 32px !important; /* Más redondeado que el estándar pero con forma definida */
  height: 42px !important;
  padding: 0 28px !important;
  box-shadow: 0 4px 12px rgba(49, 46, 129, 0.15) !important;
}

.v-dashboard:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(49, 46, 129, 0.25) !important;
}

/* Variante SOLID (Alta Proyectos) */
.v-solid {
  border-radius: 12px !important;
  font-weight: 700;
}

/* Variante OUTLINE (Alta Empleados) */
.v-outline {
  border-radius: 12px !important;
  border-width: 1px !important;
  font-weight: 700;
}

/* Variante TEXT (Minimalista total) */
.v-text {
  font-weight: 700;
  padding: 0 8px !important;
}

.v-text:hover {
  background: rgba(49, 46, 129, 0.05) !important;
}
</style>
