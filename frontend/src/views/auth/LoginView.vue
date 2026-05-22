<template>
  <div class="login-wrapper">
    <!-- Lado Izquierdo: Experiencia Inmersiva (Deep Space & Texture) -->
    <div class="brand-side d-none d-md-flex">
      <!-- SUPERGRÁFICO (Marca de agua estructural gigante) -->
      <img 
        src="/favicon.svg" 
        class="supergraphic-logo"
        alt="Fondo Estructural"
      />

      <!-- Capa de Textura de puntos -->
      <div class="texture-overlay"></div>
      
      <!-- Contenido Centrado -->
      <div class="brand-content d-flex flex-column align-center justify-center">
        <img 
          src="@/assets/img/logos/omnia_logo_white.svg" 
          alt="Omnia" 
          class="immersive-logo"
        />
      </div>
    </div>

    <!-- Lado Derecho: El Lienzo de Trabajo -->
    <div class="form-side d-flex flex-column justify-center align-center">
      
      <div class="form-container w-100">
        
        <div class="mb-10">
          <h1 class="modern-title f-page-title">Bienvenido de nuevo</h1>
        </div>

        <v-form @submit.prevent="handleLogin">

          <!-- Input Email -->
          <div class="input-group mb-6">
            <label class="premium-label">Correo electrónico</label>
            <input 
              v-model="email"
              type="email"
              placeholder="ejemplo@omnia.com"
              class="premium-input"
              :class="{ 'input-error': emailError }"
              @blur="emailTouched = true"
            />
            <!-- Mensaje de Error Inline -->
            <v-slide-y-transition>
              <div v-if="emailError" class="inline-error mt-1">
                {{ emailError }}
              </div>
            </v-slide-y-transition>
          </div>

          <!-- Input Password -->
          <div class="input-group mb-8">
            <label class="premium-label">Contraseña</label>
            <div class="password-wrapper">
              <input 
                v-model="password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="••••••••"
                class="premium-input"
                :class="{ 'input-error': passwordError }"
                @blur="passwordTouched = true"
              />
              <button 
                type="button" 
                class="eye-btn" 
                @click="showPassword = !showPassword"
                tabindex="-1"
              >
                <v-icon :icon="showPassword ? 'mdi-eye-off-outline' : 'mdi-eye-outline'" size="20" color="#64748B"></v-icon>
              </button>
            </div>
            <!-- Mensaje de Error Inline -->
            <v-slide-y-transition>
              <div v-if="passwordError" class="inline-error mt-1">
                {{ passwordError }}
              </div>
            </v-slide-y-transition>
          </div>

          <!-- Error Global del Backend (Susurro Minimalista) -->
          <v-slide-y-transition>
            <div v-if="globalError" class="global-error-text mb-4 text-center">
              {{ globalError }}
            </div>
          </v-slide-y-transition>

          <!-- Botón de Submit Inteligente (Disabled hasta estar válido) -->
          <button 
            type="submit" 
            class="premium-cta w-100 position-relative"
            :disabled="!isFormValid || authStore.loading"
          >
            <span :class="{ 'opacity-0': authStore.loading }">Iniciar Sesión</span>
            <v-progress-circular
              v-if="authStore.loading"
              indeterminate
              color="white"
              size="20"
              width="2"
              class="position-absolute"
              style="top: 50%; left: 50%; transform: translate(-50%, -50%);"
            ></v-progress-circular>
          </button>
        </v-form>
      </div>

      <div class="legal-footer">
        Uso exclusivo interno. Omnia S.L. © {{ new Date().getFullYear() }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();

const email = ref('');
const password = ref('');
const showPassword = ref(false);

const emailTouched = ref(false);
const passwordTouched = ref(false);
const globalError = ref('');

// Computed properties para UX Reactiva
const isEmailValid = computed(() => {
  return email.value.length > 0 && /.+@.+\..+/.test(email.value);
});

const isPasswordValid = computed(() => {
  return password.value.length > 0;
});

const isFormValid = computed(() => {
  return isEmailValid.value && isPasswordValid.value;
});

// Mensajes de error en tiempo real (Inline Validation)
const emailError = computed(() => {
  if (!emailTouched.value) return '';
  if (!email.value) return 'El correo electrónico es obligatorio.';
  if (!/.+@.+\..+/.test(email.value)) return 'Formato de correo inválido.';
  return '';
});

const passwordError = computed(() => {
  if (!passwordTouched.value) return '';
  if (!password.value) return 'La contraseña es obligatoria.';
  return '';
});

const handleLogin = async () => {
  if (!isFormValid.value) return;
  
  globalError.value = ''; // Reset
  
  const success = await authStore.login(email.value, password.value);
  if (success) {
    router.push({ name: 'dashboard' });
  } else {
    // Interceptamos el mensaje técnico del backend y mostramos un texto humano y preciso
    globalError.value = "Credenciales incorrectas. Vuelve a intentarlo.";
  }
};
</script>

<style scoped>
/* Layout General */
.login-wrapper {
  display: flex;
  min-height: 100vh;
  width: 100%;
  background-color: #FFFFFF;
}

/* ---------------------------------
   LADO IZQUIERDO: BRANDING INTERNO
--------------------------------- */
.brand-side {
  flex: 1.2;
  position: relative;
  background: radial-gradient(circle at 50% 50%, #1e1b4b 0%, #070617 100%);
  overflow: hidden;
}

.supergraphic-logo {
  position: absolute;
  bottom: -15%;
  left: -30%;
  height: 130%;
  opacity: 0.08; /* Textura fantasmal arquitectónica */
  z-index: 0;
  pointer-events: none;
  mask-image: radial-gradient(circle at center, black 40%, transparent 80%);
  -webkit-mask-image: radial-gradient(circle at center, black 40%, transparent 80%);
  filter: blur(1px);
}

.texture-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: radial-gradient(rgba(255, 255, 255, 0.04) 1px, transparent 1px);
  background-size: 24px 24px;
  z-index: 1;
}

/* Deep Aurora Principal (Diagonal superior-izquierda) */
.brand-side::before {
  content: '';
  position: absolute;
  top: -15%;
  left: -10%;
  width: 55vw;
  height: 55vw;
  background: radial-gradient(circle, rgba(79, 70, 229, 0.3) 0%, transparent 65%);
  border-radius: 50%;
  z-index: 0;
}

/* Deep Aurora Secundaria (Diagonal inferior-derecha, tensión elegante) */
.brand-side::after {
  content: '';
  position: absolute;
  bottom: -25%;
  right: -25%;
  width: 55vw;
  height: 55vw;
  background: radial-gradient(circle, rgba(139, 92, 246, 0.15) 0%, transparent 65%);
  border-radius: 50%;
  z-index: 0;
}

.brand-content {
  width: 100%;
  height: 100%;
  position: relative;
  z-index: 2;
}

.immersive-logo {
  max-width: 250px;
  opacity: 0.98;
  filter: drop-shadow(0 25px 50px rgba(0, 0, 0, 0.8)) drop-shadow(0 0 60px rgba(79, 70, 229, 0.3));
  transition: transform 0.5s ease;
}

.immersive-logo:hover {
  transform: scale(1.02);
}

/* ---------------------------------
   LADO DERECHO: FORMULARIO REACTIVO
--------------------------------- */
.form-side {
  flex: 1;
  background-color: #FFFFFF;
  position: relative;
  /* Padding fluido: cómodo en laptop pequeño y en monitor grande */
  padding: clamp(1.5rem, 4vw, 3rem);
}

.form-container {
  /* Fluido: nunca más de 420px, pero se adapta en pantallas pequeñas */
  max-width: clamp(320px, 90%, 420px);
  width: 100%;
}

.modern-title {
  /* clamp(mínimo, preferido, máximo): fluido en cualquier pantalla */
  font-size: clamp(1.375rem, 2.5vw, 1.875rem);
  font-weight: 600;
  color: #0F172A;
  letter-spacing: -0.03em;
}

/* Errores Inline y Global */
.inline-error {
  color: #DC2626;
  font-size: 0.75rem; /* 12px */
  font-weight: 500;
  margin-top: 6px;
}

.global-error-text {
  color: #DC2626;
  font-size: 0.75rem; /* 12px - Mismo peso visual que los inline */
  font-weight: 500;
}

.input-error {
  border-color: #FCA5A5;
  box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
}

/* Grupos de Inputs */
.input-group {
  position: relative;
}

.premium-label {
  display: block;
  font-size: 0.875rem; /* 14px */
  font-weight: 600;
  color: #334155;
  margin-bottom: 8px;
}

/* Inputs de Alta Calidad */
.premium-input {
  width: 100%;
  background-color: #FFFFFF;
  border: 1px solid #E2E8F0;
  border-radius: 16px;
  padding: 14px 16px;
  font-size: 1rem; /* 16px */
  color: #0F172A;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.015);
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  outline: none;
}

.premium-input::placeholder {
  color: #94A3B8;
  font-weight: 400;
  font-size: 0.9375rem; /* 15px */
}

/* Fix: Anular el fondo azul/amarillo de Chrome en autofill */
.premium-input:-webkit-autofill,
.premium-input:-webkit-autofill:hover,
.premium-input:-webkit-autofill:focus {
  -webkit-box-shadow: 0 0 0px 1000px #FFFFFF inset;
  -webkit-text-fill-color: #0F172A;
  transition: background-color 5000s ease-in-out 0s;
}

.premium-input:hover:not(.input-error) {
  border-color: #CBD5E1;
}

.premium-input:focus:not(.input-error) {
  border-color: #312E81;
  box-shadow: 0 0 0 3px rgba(49, 46, 129, 0.15), inset 0 2px 4px rgba(0, 0, 0, 0.015);
}

.password-wrapper {
  position: relative;
}

.eye-btn {
  position: absolute;
  right: 12px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 6px;
  border-radius: 6px;
  transition: background-color 0.2s ease;
}

.eye-btn:hover {
  background-color: #F1F5F9;
}

/* Botón Sólido Inteligente (Empieza apagado) */
.premium-cta {
  background-color: #312E81;
  color: #FFFFFF;
  border: none;
  border-radius: 12px;
  height: 38px;
  padding: 0 24px;
  font-size: 1rem;
  font-weight: 600;
  letter-spacing: 0.01em;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(49, 46, 129, 0.2);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.premium-cta:hover:not(:disabled) {
  background-color: #1E1B4B;
  box-shadow: 0 8px 20px rgba(49, 46, 129, 0.3);
  transform: translateY(-2px);
}

.premium-cta:active:not(:disabled) {
  transform: translateY(1px);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.05);
}

/* Estado Mudo/Apagado (Smart CTA) */
.premium-cta:disabled {
  background-color: #E2E8F0;
  color: #94A3B8;
  box-shadow: none;
  cursor: not-allowed;
  transform: none;
}

.legal-footer {
  position: absolute;
  bottom: 32px;
  font-size: 0.75rem; /* 12px */
  color: #94A3B8;
  text-align: center;
  width: 100%;
  left: 0;
}

/* --- RESPONSIVE UNIVERSAL --- */

/* Pantallas pequeñas (laptops 768px-1024px) */
@media (max-width: 1024px) {
  /* El panel de marca se reduce proporcionalmente */
  .brand-side {
    flex: 1;
  }

  .immersive-logo {
    max-width: 180px;
  }
}

/* Pantallas muy pequeñas / portrait (< 768px): sólo formulario */
@media (max-width: 767px) {
  .form-side {
    justify-content: flex-start;
    padding-top: clamp(2rem, 8vh, 4rem);
  }

  .form-container {
    max-width: 100%;
  }

  .legal-footer {
    position: relative;
    bottom: 0;
    margin-top: 3rem;
  }
}

/* Monitores grandes (1600px+): el formulario no se estira demasiado */
@media (min-width: 1600px) {
  .brand-side {
    flex: 1.4;
  }

  .form-container {
    max-width: 480px;
  }
}

.opacity-0 {
  opacity: 0;
}
</style>
