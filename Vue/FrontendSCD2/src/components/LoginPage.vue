<template>
  <div class="login-container">
    <h1>Login</h1>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="name">Name:</label>
        <input id="name" v-model="name" type="text" required />
      </div>
      <div>
        <label for="email">Email:</label>
        <input id="email" v-model="email" type="email" required />
      </div>
      <button type="submit">Login</button>
      <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
    </form>
  </div>
</template>

<script>
import axios from "axios";
import { ref } from "vue";
import { useRouter } from "vue-router";

export default {
  setup() {
    const name = ref("");
    const email = ref("");
    const errorMessage = ref("");
    const router = useRouter();

    const handleLogin = async () => {
      try {
        const response = await axios.post("http://localhost:8083/login", {
          name: name.value,
          email: email.value,
        });
        localStorage.setItem("user", JSON.stringify(response.data));
        router.push("/main");
      } catch (error) {
        if (error.response && error.response.status === 401) {
          errorMessage.value = "Invalid name or email.";
        } else {
          errorMessage.value = "An error occurred. Please try again later.";
        }
      }
    };

    return {
      name,
      email,
      errorMessage,
      handleLogin,
    };
  },
};
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 50px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  background: #f9f9f9;
}

form div {
  margin-bottom: 15px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  width: 100%;
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #45a049;
}

.error {
  color: red;
  margin-top: 10px;
}
</style>