<script>
import { useRouter } from "vue-router";
import JourneyTunesTitle from "@/components/JourneyTunesTitle.vue";
import axiosClient from "@/clients/axiosClient";
import {useSnackbar} from "@/components/useSnackbar";

export default {
  components: { JourneyTunesTitle },
  data() {
    return {
      form: false,
      dialogVisible: false,
      username: null,
      email: null,
      showSnackbar : useSnackbar(),
      password: null,
      loading: false,
    };
  },

  methods: {
    moveToLogin() {
      this.$router.push('/login');
    },
    async onSubmit() {
      if (!this.form) return;

      this.loading = true;
      const userData = {
        name: this.username,
        email: this.email,
        password: this.password,
      };

      try {
        await axiosClient.post('users/api/v1/create', userData);
        this.dialogVisible = true
      } catch (error) {
        useSnackbar().showSnackbar("Username or email already registered!", 2500)
        console.error("There was an error creating the user:", error.response.data);

      } finally {
        this.loading = false;
      }
    },
    required(v) {
      return !!v || 'Field is required';
    },
  },
  setup() {
    const router = useRouter();
    return { router };
  },
}
</script>

<template>
  <div>
    <v-dialog
      v-model="dialogVisible"
      class="pl-16"
      transition="dialog-top-transition"
      width="auto"
      height="auto"

    >
      <v-card>
        <v-toolbar color="indigo">
          <v-toolbar-title >JourneyTunes</v-toolbar-title>
        </v-toolbar>
        <v-card-text>
          <div class="  pt-4 text-center text-h-6">
            <p class="pb-2">Successfully registered! 🎉</p>
            <p>Verification email has been sent! 📫 </p> </div>
        </v-card-text>
        <v-card-actions class="justify-center">
          <v-btn
            @click="moveToLogin">Back to Login</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <div>
      <journey-tunes-title class="pt-10">
      </journey-tunes-title>

      <div class="text-center">
        <v-sheet class="pt-24">
          <v-card class="mx-auto pb-2 pt-2 pl-4 pr-4 maincard" max-height="400"
                  elevation="8"
                  max-width="250">
            <v-form
              v-model="form"
              @submit.prevent="onSubmit">

              <div class="d-flex flex-column align-center pt-2">
                <v-text-field
                  v-model="username"
                  :readonly="loading"
                  :rules="[required]"
                  prepend-inner-icon="mdi-account"
                  label="Username"
                  class="mb-4"
                ></v-text-field>

                <v-text-field
                  v-model="password"
                  :readonly="loading"
                  :rules="[required]"
                  label="Password"
                  type="password"
                  prepend-inner-icon="mdi-lock"
                  placeholder="Enter your password"
                  class="mb-4"
                ></v-text-field>

                <v-text-field
                  v-model="email"
                  :readonly="loading"
                  :rules="[required]"
                  typeof="email"
                  prepend-inner-icon="mdi-email"
                  label="Email"
                  placeholder="Email"
                  class="mb-4"
                ></v-text-field>
              </div>

              <div class="pt-4 pb-2">
                <v-btn
                  :disabled="!form"
                  :loading="loading"
                  block
                  color="indigo"
                  small
                  type="submit"
                  variant="elevated"
                >
                  Register
                </v-btn>

                <v-btn
                  :loading="loading"
                  block
                  color="indigo"
                  size="small"
                  variant="elevated"
                  @click="moveToLogin"
                  class="mt-2"
                >Back To Login
                </v-btn>
              </div>

            </v-form>
          </v-card>
        </v-sheet>
      </div>
    </div>
  </div>
</template>


<style scoped>

.maincard{
  border-radius: 1em;
  border: solid #943bd2;
}

.v-text-field {
  width: 95%;
  height: 50px;
  color: indigo;
  background-color: white;
  padding-bottom: 4em;
}

.v-text-field >>> input {

  font-size: 0.8em;
  color: black;
}

</style>
