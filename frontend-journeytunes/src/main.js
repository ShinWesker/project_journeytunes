/**
 * main.js
 *
 * Bootstraps Vuetify and other plugins then mounts the App`
 */

// Plugins
import './style.css'
import '@mdi/font/css/materialdesignicons.css'
import { registerPlugins } from '@/plugins'
import router from "@/router/index.js";
import store from "@/store/index.js"

// Components
import App from './App.vue'


store.dispatch('initializeAuthState');
// Composables
import { createApp } from 'vue'

const app = createApp(App)
  .use(router)
  .use(store)


registerPlugins(app)

app.mount('#app')
