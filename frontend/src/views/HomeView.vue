<script setup lang="ts">
import { logout } from "../api";
import { homePageTokenInit } from "../utils/JwtUtils";
import { onMounted } from "vue";

onMounted(() => {
  homePageTokenInit();
});

const handleLogout = () => {
  logout(() => {
  });
};
</script>

<template>
  <div class="center-container">
    <el-button @click="handleLogout" type="primary">logout</el-button>
  </div>
</template>

<style scoped>
.center-container {
  width: 100vw;
  height: 100vh;

  display: flex;
  justify-content: center;
  align-items: center;
}
</style>