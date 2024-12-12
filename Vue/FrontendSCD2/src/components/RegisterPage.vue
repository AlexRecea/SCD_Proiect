<template>
    <div class="register-page">
      <h1>Register</h1>
      <form @submit.prevent="registerUser" class="register-form">
        <div class="form-group">
          <input
            type="text"
            id="name"
            v-model="formData.name"
            required
            placeholder="Enter your name"
          />
        </div>
        <div class="form-group">
          <input
            type="email"
            id="email"
            v-model="formData.email"
            required
            placeholder="Enter your email"
          />
        </div>
        <div class="form-group">
          <input
            type="email"
            id="confirmEmail"
            v-model="formData.confirmEmail"
            required
            placeholder="Confirm your email"
          />
        </div>
        <div class="button-group">
          <button type="submit" class="submit-button">Register</button>
          <button type="button" @click="navigateToLogin" class="cancel-button">Cancel</button>
        </div>
      </form>
    </div>
  </template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      formData: {
        name: "",
        email: "",
        confirmEmail: "",
      },
    };
  },
  methods: {
    async registerUser() {
      if (this.formData.email !== this.formData.confirmEmail) {
        alert("Emails do not match!");
        return;
      }

      try {
        // Verifică dacă utilizatorul există deja
        const response = await axios.get("http://localhost:8083/checkUser", {
          params: {
            name: this.formData.name,
            email: this.formData.email,
          },
        });

        const { nameExists, emailExists } = response.data;

        // Afișează erori personalizate
        if (nameExists && emailExists) {
          alert("Both the name and email already exist!");
          return;
        } else if (nameExists) {
          alert("The name already exists!");
          return;
        } else if (emailExists) {
          alert("The email already exists!");
          return;
        }

        // Dacă utilizatorul nu există, continuăm cu înregistrarea
        const payload = {
          name: this.formData.name,
          email: this.formData.email,
        };

        const registerResponse = await axios.post("http://localhost:8083/createUser", payload);

        if (registerResponse.status === 201 || registerResponse.status === 200) {
          alert("User registered successfully!");
          this.$router.push("/"); // Redirecționează către pagina de login
        } else {
          alert("Failed to register user. Please try again.");
        }
      } catch (error) {
        console.error("Error during registration:", error);
        alert("An error occurred. Please try again.");
      }
    },
    navigateToLogin() {
      this.$router.push("/"); // Navighează înapoi la pagina de login
    },
  },
};
</script>

<style scoped>
.register-page {
  text-align: center;
  padding: 20px;
  background-color: #fffaf5;
  color: #5a3e2f;
}

h1 {
  color: #b55d2c;
  margin-bottom: 20px;
}

.register-form {
  width: 700px;
  margin: 0 auto;
  padding: 20px;
  background-color: #ffe4cc;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  align-items: center;
}

.form-group {
  margin-bottom: 15px;
  text-align: left;
  display: flex;
    justify-content: center;
}

input[type="text"],
input[type="email"] {
  width: 90%;
  padding: 10px;
  border: 1px solid #b55d2c;
  border-radius: 5px;
  font-size: 14px;
}

.button-group {
  display: flex;
  justify-content: space-between;
}

.submit-button {
  padding: 10px 20px;
  background-color: #b55d2c;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.submit-button:hover {
  background-color: #933f1e;
}

.cancel-button {
  padding: 10px 20px;
  background-color: #ccc;
  color: #5a3e2f;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.cancel-button:hover {
  background-color: #bbb;
}
</style>