// Utility class for making requests to server
class Client {
    static IP = 'change'
    static PORT = 'change'
    static CONTEXT = 'api'
    static VERSION = 'v1'

    static ROOT_PATH = `http://${IP}:${PORT}/${CONTEXT}/${VERSION}/`

    static authToken; // Probably not the right place to store this

    static async getUserInfo() {
        const userInfo = fetch(ROOT_PATH + 'user', {
            method: 'GET'
        })
        .then(response => response.json())
        .catch(error => {
            console.error('Failed to fetch user info:', error)
        });
        return userInfo
    }

    static async setUserInfo(userInfo) {
        const userInfo = fetch(ROOT_PATH + 'user', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userInfo)
        })
        .then(response => response.json())
        .catch(error => {
            console.error('Failed to set user info:', error)
        });
        return userInfo
    }

    static async createUser(accountInfo) {
        const username = fetch(ROOT_PATH + 'create', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(accountInfo)
        })
        .then(response => response.json())
        .catch(error => {
            console.error('Failed to create user:', error)
        });
        return username
    }

    static async logIn(credentials) {
        const authToken = fetch(ROOT_PATH + 'login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(credentials)
        })
        .then(response => response.json())
        .catch(error => {
            console.error('Failed to log in:', error)
        });
        this.authToken = authToken
    }
}