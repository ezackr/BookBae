import axios from 'axios';
/** Utility class for making requests to server */
class Client {
  static IP = '10.0.2.2';
  static PORT = '8080';
  static CONTEXT = 'api';
  static VERSION = 'v1';

  static ROOT_PATH = `http://${Client.IP}:${Client.PORT}/${Client.CONTEXT}/${Client.VERSION}`;

  static authToken; // Probably not the right place to store this

  /**
   * Gets the user info given the user's authToken
   * @return {{email, name, preferredGender, gender,
   *          favGenre, birthday, bio, zipcode}}, or null for failure
   */
  static async getUserInfo() {
    return await axios({
      baseURL: Client.ROOT_PATH,
      url: '/user',
      method: 'get',
      headers: {Authorization: 'Bearer ' + Client.authToken},
    })
      .then(response => response.data)
      .catch(response => {
        console.log(response);
        return null;
      });
  }

  /**
   * Updates user info
   * @param {{email, name, preferredGender, gender,
   *          favGenre, birthday, bio, zipcode}} userInfo
   * @return {{email, name, preferredGender, gender,
   *          favGenre, birthday, bio, zipcode}} the server's (new) user info, or null for failure
   */
  static async setUserInfo(userInfo) {
    return await axios({
      baseURL: Client.ROOT_PATH,
      url: '/user',
      method: 'put',
      headers: {Authorization: 'Bearer ' + Client.authToken},
      data: userInfo,
    })
      .then(response => response.data)
      .catch(response => {
        console.log(response);
        return null;
      });
  }

  /**
   * Creates a new user account
   * @param {string} email - the email to associate new account with
   * @param {string} password - the password that the user will use to access account
   * @return {boolean} whether account was successfully created
   */
  static async createUser(email, password) {
    return await axios({
      baseURL: Client.ROOT_PATH,
      url: '/create',
      method: 'post',
      data: {email: email, password: password},
    })
      .then(response => {
        // success
        return true;
      })
      .catch(response => {
        console.log(response);
        return false;
      });
  }

  /**
   * Attempts to log in to an account with the given email and password
   * @param {string} email - the email associated with the account
   * @param {string} password - the password associated with the account
   * @return {boolean} whether login was successful
   */
  static async logIn(email, password) {
    return await axios({
      baseURL: Client.ROOT_PATH,
      url: '/login',
      method: 'post',
      data: {email: email, password: password},
    })
      .then(response => {
        // success
        Client.authToken = response.data.authToken;
        return true;
      })
      .catch(response => {
        // failure
        console.log(response);
        Client.authToken = null;
        return false;
      });
  }

  /**
   * Gets a list of recommended potential matches
   * @return {[{userid, name, preferredGender, gender, favGenre, birthday, bio}]}, or null for failure
   */
  static async getPotentialMatches() {
    return await axios({
      baseURL: Client.ROOT_PATH,
      url: '/recommends',
      method: 'get',
      headers: {Authorization: 'Bearer ' + Client.authToken},
    })
      .then(response => response.data)
      .catch(response => {
        console.log(response);
        return null;
      });
  }

  /**
   * Likes user with given id
   * @param {string} userid - the user to like
   * @return {boolean} whether like was successful
   */
  static async sendLike(userid) {
    return await axios({
      baseURL: Client.ROOT_PATH,
      url: '/like',
      method: 'get',
      headers: {Authorization: 'Bearer ' + Client.authToken},
      data: {userid: userid},
    })
      .then(response => true)
      .catch(response => {
        console.log(response);
        return false;
      });
  }

  /**
   * Gets existing matches for chat
   * @return {{displayName, photoUrl, lastMessage, likeId}}, or null for failure
   */
  static async getChats() {
    await axios({
      baseURL: Client.ROOT_PATH,
      url: '/chats',
      method: 'get',
      headers: {Authorization: 'Bearer ' + Client.authToken},
    })
      .then(response => response.data)
      .catch(response => {
        console.log(response);
        return null;
      });
  }

  static async getMessages(matchId) {
    await axios({
      baseURL: Client.ROOT_PATH,
      url: '/chats/' + matchId,
      method: 'get',
      headers: {Authorization: 'Bearer ' + Client.authToken},
    })
      .then(response => response.data)
      .catch(response => {
        console.log(response);
        return null;
      });
  }

  static async sendMessage(matchId, message) {
    await axios({
      baseURL: Client.ROOT_PATH,
      url: '/chats/' + matchId,
      method: 'post',
      headers: {Authorization: 'Bearer ' + Client.authToken},
      data: {text: message}
    })
      .then(response => response.data)
      .catch(response => {
        console.log(response);
        return null;
      });
  }
}

export default Client;
