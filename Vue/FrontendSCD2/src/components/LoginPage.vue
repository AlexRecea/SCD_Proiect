<template>
  <div class="login-container">
    <div class="login-card">
      <h1>Welcome Back!</h1>
      <p>Please log in to access your account.</p>

      <label for="name">Name</label>
      <input v-model="name" type="text" id="name" placeholder="Enter your name" />

      <label for="email">Email</label>
      <input v-model="email" type="email" id="email" placeholder="Enter your email" />

      <button class="login-btn" @click="login">Login</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      name: "",
      email: "",
    };
  },
  methods: {
  async login() {
    try {
      const payload = { name: this.name, email: this.email };
      const response = await axios.post("http://localhost:8083/login", payload);

      if (response.status === 200) {
        const userIdResponse = await axios.get("http://localhost:8083/getUserId", {
          params: { name: this.name, email: this.email },
        });

        const user = {
          id: userIdResponse.data,
          name: this.name,
          email: this.email,
        };

        // Stochează utilizatorul în localStorage
        localStorage.setItem("currentUser", JSON.stringify(user));

        // Navighează direct către pagina principală
        this.$router.push("/main");
      } else {
        alert("Login failed.");
      }
    } catch (error) {
      console.error("Error during login:", error);
      alert("Login error. Please try again.");
    }
  },
},
};
</script>

<style scoped>
/* General styles */
body {
  margin: 0;
  font-family: Arial, sans-serif;
  background-color: #fff4e6;
  color: #4a4a4a;
}

/* Login container styles */
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #fff4e6;
}

.login-card {
  background-color: #ffe5cc;
  border: 2px solid #d35400;
  border-radius: 10px;
  padding: 30px;
  width: 350px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
}

.login-card h1 {
  margin-bottom: 15px;
  color: #d35400;
}

.login-card p {
  margin-bottom: 20px;
  color: #7f8c8d;
  font-size: 0.9rem;
}

.login-card label {
  display: block;
  margin: 10px 0 5px;
  font-size: 1rem;
  color: #4a4a4a;
}

.login-card input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #d35400;
  border-radius: 5px;
  font-size: 1rem;
}

.login-btn {
  background-color: #d35400;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
  font-size: 1rem;
  cursor: pointer;
}

.login-btn:hover {
  background-color: #e67e22;
}
</style>