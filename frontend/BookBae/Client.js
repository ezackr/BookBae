import axios from 'axios'
/** Utility class for making requests to server */
class Client {
  static IP = '127.0.0.1';
  static PORT = '8080';
  static CONTEXT = 'api';
  static VERSION = 'v1';

  static ROOT_PATH = `http://${Client.IP}:${Client.PORT}/${Client.CONTEXT}/${Client.VERSION}`;

  static authToken; // Probably not the right place to store this

  /**
   * Gets the user info given the user's authToken
   * @return {{email, name, preferredGender, gender,
   *          favGenre, birthday, bio, zipcode}}
   */
  static async getUserInfo() {
    return await fetch(Client.ROOT_PATH + 'user', {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + Client.authToken,
      },
    })
      .then(response => response.json())
      .catch(error => {
        console.error('Failed to fetch user info:', error);
      });
  }

  /**
   * Updates user info
   * @param {{email, name, preferredGender, gender,
   *          favGenre, birthday, bio, zipcode}} userInfo
   * @return {{email, name, preferredGender, gender,
   *          favGenre, birthday, bio, zipcode}} the server's (new) user info
   */
  static async setUserInfo(userInfo) {
    return await fetch(Client.ROOT_PATH + 'user', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + Client.authToken,
      },
      body: JSON.stringify(userInfo),
    })
      .then(response => response.json())
      .catch(error => {
        console.error('Failed to set user info:', error);
      });
  }

  /**
   * Creates a new user account
   * @param {string} email - the email to associate new account with
   * @param {string} password - the password that the user will use to access account
   */
  static async createUser(email, password) {
    const credentials = {email, password};
    return await fetch(Client.ROOT_PATH + 'create', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(credentials),
    })
      .then(response => response.status)
      .catch(error => {
        console.error('Failed to create user:', error);
      });
  }

  /**
   * Attempts to log in to an account with the given email and password
   * @param {string} email - the email associated with the account
   * @param {string} password - the password associated with the account
   * @return {boolean} whether login was successful
   */
  static async logIn(email, password) {
//    const credentials = {email, password};
//    Client.authToken = await fetch(Client.ROOT_PATH + 'login', {
//      method: 'POST',
//      headers: {
//        'Content-Type': 'application/json',
//      },
//      body: JSON.stringify(credentials),
//    })
    return await axios({
      baseURL: Client.ROOT_PATH,
      url: '/login',
      method: 'post',
      data: { email: email, password: password }
    })
      .then(response => { // success
        Client.authToken = response.data.authToken;
        return true;
      })
      .catch(response => { // failure
        Client.authToken = null;
        return false;
      });
  }

  /**
   * Gets a list of recommended potential matches
   * @return {[{userid, name, preferredGender, gender, favGenre, birthday, bio}]}
   */
  static async getPotentialMatches() {
    const potentialMatches = await fetch(Client.ROOT_PATH + 'recommends', {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + Client.authToken,
      },
    })
      .then(response => response.json())
      .catch(error => {
        console.error('Failed to get potential matches', error);
      });
  }

  /**
   * Likes user with given id
   * @param {string} userid - the user to like
   */
  static async sendLike(userid) {
    await fetch(Client.ROOT_PATH + 'like', {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + Client.authToken,
      },
      body: JSON.stringify({userid}),
    })
      .then(response => response.json())
      .catch(error => {
        console.error('Failed to send like', error);
      });
  }

  /**
   * Gets existing matches for chat
   * @return {{displayName, photoUrl, lastMessage, likeId}}
   */
  static async getChats() {
    const chats = await fetch(Client.ROOT_PATH + 'chats', {
      method: 'GET',
      headers: {
        Authorization: 'Bearer ' + Client.authToken,
      },
    })
      .then(response => response.json())
      .catch(error => {
        console.error('Failed to get chats', error);
      });
  }
}

export default Client;
