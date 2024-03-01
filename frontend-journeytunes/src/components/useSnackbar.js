import { ref } from 'vue';

const snackbar = ref(false);
const message = ref('');
const timeout = ref(3000);

export function useSnackbar() {
  function showSnackbar(msg, duration = 3000) {
    message.value = msg;
    timeout.value = duration;
    snackbar.value = true;
  }

  function hideSnackbar() {
    snackbar.value = false;
  }

  return { snackbar, message, timeout, showSnackbar, hideSnackbar };
}
