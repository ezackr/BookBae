// Utility class for making requests to server
class Client {
  static IP = '127.0.0.1';
  static PORT = '8080';
  static CONTEXT = 'api';
  static VERSION = 'v1';

  static ROOT_PATH = `http://${Client.IP}:${Client.PORT}/${Client.CONTEXT}/${Client.VERSION}/`;

  static authToken; // Probably not the right place to store this

  static async getUserInfo() {
    return fetch(Client.ROOT_PATH + 'user', {
      method: 'GET',
    })
      .then(response => response.json())
      .catch(error => {
        console.error('Failed to fetch user info:', error);
      });
  }

  static async setUserInfo(loginInfo) {
    return await fetch(Client.ROOT_PATH + 'user', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(loginInfo),
    })
      .then(response => response.json())
      .catch(error => {
        console.error('Failed to set user info:', error);
      });
  }

  static async createUser(accountInfo) {
    return await fetch(Client.ROOT_PATH + 'create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(accountInfo),
    })
      .then(response => response.status)
      .catch(error => {
        console.error('Failed to create user:', error);
      });
  }

  static async logIn(credentials) {
    this.authToken = await fetch(Client.ROOT_PATH + 'login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    })
      .then(response => response.json())
      .catch(() => {});
  }
}

export default Client;
