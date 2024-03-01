// useLocalStorage.js
import { ref, watchEffect } from 'vue';

export function useLocalStorage(key) {
  const storedValue = ref(localStorage.getItem(key));

  watchEffect(() => {
    storedValue.value = localStorage.getItem(key);
  });

  return { storedValue };
}
