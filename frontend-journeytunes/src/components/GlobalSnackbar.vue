<!-- GlobalSnackbar.vue -->
<template>
    <v-snackbar v-model="snackbar" :timeout="timeout" location-strategy="static">
      <div>
        {{ message }}
      </div>

      <template v-slot:actions>
        <v-btn
          color="blue"
          variant="text"
          @click="snackbar = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
</template>

<script setup>
import {useSnackbar} from "@/components/useSnackbar";

const { snackbar, message, timeout, hideSnackbar } = useSnackbar();
</script>
