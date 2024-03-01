<template>
  <div class="flex-col justify-center align-center ">
    <div class=" flex justify-center">
      <h1 class="title text-8xl">
        JourneyTunes
      </h1>
    </div>
  </div>
</template>

<style scoped>

.title {
  display: inline-block;
  animation: rainbowFade 8s infinite linear;
  color: initial;
}

@keyframes jumpFade {
  0%, 100% {
    transform: translateY(0);
    opacity: 1;
  }
  50% {
    transform: translateY(-10px);
    opacity: 0.5;
  }
}

@keyframes rainbowFade {
  0% {
    color: purple
  }
  25% {
    color: indigo
  }
  50% {
    color: blue
  }
  75% {
    color: indigo
  }
  100% {
    color: purple
  }
}
</style>
