import Client from '../Client';
import axios from 'axios';
import AxiosMockAdapter from 'axios-mock-adapter'

// suppress error logs
console.log = () => {}

describe('Client', () => {
    let mock;
    beforeAll(() => {
        mock = new AxiosMockAdapter(axios);
    });

    afterEach(() => {
        mock.reset();
    });

    describe('logIn', () => {
        test('makes correct request', async () => {
            await Client.logIn('fake@email.com', 'mypassword');

            expect(mock.history.post[0].data)
                .toBe(JSON.stringify({
                    email: 'fake@email.com',
                    password: 'mypassword'
                }));
        });

        test('returns true and sets authToken when successful', async () => {
            mock.onPost(Client.ROOT_PATH + '/login').replyOnce(200, {authToken: 'auth1234'});
            const success = await Client.logIn('e', 'p');
            expect(success).toBeTruthy();
            expect(Client.authToken).toBe('auth1234');
        });

        test('returns false when unsuccessful', async () => {
            mock.onPost(Client.ROOT_PATH + '/login').replyOnce(401);
            const success = await Client.logIn('e', 'p');
            expect(success).toBeFalsy();
            expect(Client.authToken).toBeFalsy();
        });
    });

    describe('createUser', () => {
        test('makes correct request', async () => {
            await Client.createUser('fake@email.com', 'mypassword');

            expect(mock.history.post[0].data)
                .toBe(JSON.stringify({
                    email: 'fake@email.com',
                    password: 'mypassword'
                }));
        });

        test('returns true when successful', async () => {
            mock.onPost(Client.ROOT_PATH + '/create').replyOnce(200);
            const success = await Client.createUser('e', 'p');
            expect(success).toBeTruthy();
        });

        test('returns false when unsuccessful', async () => {
            mock.onPost(Client.ROOT_PATH + '/create').replyOnce(400);
            const success = await Client.createUser('e', 'p');
            expect(success).toBeFalsy();
        });
    });

    describe('getUserInfo', () => {
        test('makes correct request with authToken', async () => {
            Client.authToken = 'myauthtoken';
            await Client.getUserInfo();
            expect(mock.history.get[0].headers.Authorization).toBe('Bearer myauthtoken');
        });

        test('returns server response when successful', async () => {
            const expectedInfo =  {
                email: 'myemail',
                name: 'myname',
                gender: 'mygender',
                favGenre: 'myfavgenre',
                birthday: 'mybirthday',
                bio: 'mybio',
                zipcode: 'myzipcode'
            }
            mock.onGet(Client.ROOT_PATH + '/user').replyOnce(200, expectedInfo);
            const actualInfo = await Client.getUserInfo();
            expect(actualInfo).toStrictEqual(expectedInfo);
        });

        test('returns null when unsuccessful', async () => {
            mock.onGet(Client.ROOT_PATH + '/user').replyOnce(400);
            const response = await Client.getUserInfo();
            expect(response).toBeNull();
        });
    });

    describe('setUserInfo', () => {
        test('makes correct request with authToken', async () => {
            Client.authToken = 'myauthtoken';
            await Client.setUserInfo();
            expect(mock.history.put[0].headers.Authorization).toBe('Bearer myauthtoken');
        });

        test('returns new user info when successful', async () => {
           mock.onPut(Client.ROOT_PATH + '/user').replyOnce(200, {myUserInfo: 'userinfo'});
           const response = await Client.setUserInfo({});
           expect(response).toStrictEqual({myUserInfo: 'userinfo'})
        });

        test('returns null when unsuccessful', async () => {
            mock.onPut(Client.ROOT_PATH + '/user').replyOnce(400);
            const response = await Client.setUserInfo({});
            expect(response).toBeNull();
        });
    });

    describe('getPotentialMatches', () => {
        test('makes correct request with authToken', async () => {
            Client.authToken = 'myauthtoken';
            await Client.getPotentialMatches();
            expect(mock.history.get[0].headers.Authorization).toBe('Bearer myauthtoken');
        });

        test('returns matches when successful', async () => {
            mock.onGet(Client.ROOT_PATH + '/recommends').replyOnce(200, {myMatches: 'match'});
            const response = await Client.getPotentialMatches();
            expect(response).toStrictEqual({myMatches: 'match'});
        });

        test('returns null when unsuccessful', async () => {
            mock.onGet(Client.ROOT_PATH + '/recommends').replyOnce(400);
            const response = await Client.getPotentialMatches();
            expect(response).toBeNull();
        })
    });

    describe('sendLike', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.sendLike('1234');
            expect(mock.history.put[0].headers.Authorization).toBe('Bearer myauthtoken');
        });

        test('returns true when successful', async () => {
            mock.onPut(Client.ROOT_PATH + '/like').replyOnce(200);
            const response = await Client.sendLike('1234');
            expect(response).toBe(true);
        });

        test('returns false when unsuccessful', async () => {
            mock.onPut(Client.ROOT_PATH + '/like').replyOnce(400);
            const response = await Client.sendLike('1234');
            expect(response).toBe(false);
        })
    });

    describe('getChats', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.getChats();
            expect(mock.history.get[0].headers.Authorization).toBe('Bearer myauthtoken');
        });

        test('returns chats when successful', async () => {
            mock.onGet(Client.ROOT_PATH + '/chats').replyOnce(200, {chats: 'chats'});
            const response = await Client.getChats();
            expect(response).toStrictEqual({chats: 'chats'});
        })

        test('returns null when unsuccessful', async () => {
            mock.onGet(Client.ROOT_PATH + '/chats').replyOnce(400);
            const response = await Client.getChats();
            expect(response).toBeNull();
        })
    });

    describe('getMessages', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.getMessages('1234');
            expect(mock.history.get[0].headers.Authorization).toBe('Bearer myauthtoken');
            expect(mock.history.get[0].url).toBe('/chats/1234');
        });

        test('returns messages when successful', async () => {
            mock.onGet(Client.ROOT_PATH + '/chats/1234').replyOnce(200, {messages: 'messages'});
            const response = await Client.getMessages('1234');
            expect(response).toStrictEqual({messages: 'messages'});
        });

        test('returns null when unsuccessful', async () => {
            mock.onGet(Client.ROOT_PATH + '/chats/1234').replyOnce(400);
            const response = await Client.getMessages('1234');
            expect(response).toBeNull();
        })
    });

    describe('sendMessage', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.sendMessage('1234');
            expect(mock.history.post[0].headers.Authorization).toBe('Bearer myauthtoken');
            expect(mock.history.post[0].url).toBe('/chats/1234');
        });

        test('returns true when successful', async () => {
            mock.onPost(Client.ROOT_PATH + '/chats/1234').replyOnce(200);
            const response = await Client.sendMessage('1234');
            expect(response).toBe(true);
        });

        test('returns false when unsuccesful', async () => {
            mock.onPost(Client.ROOT_PATH + '/chats/1234').replyOnce(400);
            const response = await Client.sendMessage('1234');
            expect(response).toBe(false);
        });
    });

    describe('emailIsUsed', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.emailIsUsed('myemail@email.com');
            expect(mock.history.get[0].params.email).toBe('myemail@email.com');

        });

        test('returns boolean response', async () => {
            mock.onGet(Client.ROOT_PATH + '/email').replyOnce(200, {doesEmailExist: true});
            const used = await Client.emailIsUsed('myemail@email.com');
            expect(used).toBe(true);
        })

        test('returns null on failure', async () => {
            mock.onGet(Client.ROOT_PATH + '/email').replyOnce(400);
            const used = await Client.emailIsUsed('myemail@email.com');
            expect(used).toBeNull();
        });
    });

    describe('getBooks', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.getBooks();
            expect(mock.history.get[0].headers.Authorization).toBe('Bearer myauthtoken');
        });

        test('returns book IDs as a list of strings', async () => {
            mock.onGet(Client.ROOT_PATH + '/book/get').replyOnce(200, {entries: [{bookId: '1234'}, {bookId: '2345'}, {bookId: '3456'}]});
            const books = await Client.getBooks();
            expect(books).toStrictEqual(['1234', '2345', '3456']);
        });

        test('returns empty list when there are no books', async () => {
            mock.onGet(Client.ROOT_PATH + '/book/get').replyOnce(200, {entries: []});
            const books = await Client.getBooks();
            expect(books).toStrictEqual([]);
        });

        test('returns null on failure', async () => {
            mock.onGet(Client.ROOT_PATH + '/book/get').replyOnce(400, []);
            const books = await Client.getBooks();
            expect(books).toBeNull();
        });
    });

    describe('addBooks', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.addBooks(['1234', '2345']);
            expect(mock.history.put[0].headers.Authorization).toBe('Bearer myauthtoken');
            expect(mock.history.put[0].data).toBe(JSON.stringify({entries: [{bookId: '1234'}, {bookId: '2345'}]}));
        })

        test('returns updated book IDs as a list of strings', async () => {
            mock.onPut(Client.ROOT_PATH + '/book/add').replyOnce(200, {entries: [{bookId: '1234'}, {bookId: '2345'}, {bookId: '3456'}]});
            const books = await Client.addBooks([]);
            expect(books).toStrictEqual(['1234', '2345', '3456']);
        });

        test('returns empty list when there are no books', async () => {
            mock.onPut(Client.ROOT_PATH + '/book/add').replyOnce(200, {entries: []});
            const books = await Client.addBooks([]);
            expect(books).toStrictEqual([]);
        });

        test('returns null on failure', async () => {
            mock.onPut(Client.ROOT_PATH + '/book/add').replyOnce(400, []);
            const books = await Client.addBooks([]);
            expect(books).toBeNull();
        })
    });

    describe('removeBooks', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.removeBooks(['1234', '2345']);
            expect(mock.history.put[0].headers.Authorization).toBe('Bearer myauthtoken');
            expect(mock.history.put[0].data).toBe(JSON.stringify({entries: [{bookId: '1234'}, {bookId: '2345'}]}));
        })

        test('returns updated book IDs as a list of strings', async () => {
            mock.onPut(Client.ROOT_PATH + '/book/remove').replyOnce(200, {entries: [{bookId: '1234'}, {bookId: '2345'}, {bookId: '3456'}]});
            const books = await Client.removeBooks([]);
            expect(books).toStrictEqual(['1234', '2345', '3456']);
        });

        test('returns empty list when there are no books', async () => {
            mock.onPut(Client.ROOT_PATH + '/book/remove').replyOnce(200, {entries: []});
            const books = await Client.removeBooks([]);
            expect(books).toStrictEqual([]);
        });

        test('returns null on failure', async () => {
            mock.onPut(Client.ROOT_PATH + '/book/remove').replyOnce(400);
            const books = await Client.removeBooks([]);
            expect(books).toBeNull();
        })
    });
})