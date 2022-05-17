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
        })
    });

    describe('getUserInfo', () => {
        test('makes correct request with authToken', async () => {
            Client.authToken = 'myauthtoken';
            await Client.getUserInfo();
            expect(mock.history.get[0].headers.Authorization).toBe('Bearer myauthtoken');
        })
    });

    describe('setUserInfo', () => {

    });

    describe('getPotentialMatches', () => {

    });

    describe('sendLike', () => {

    });

    describe('getChats', () => {

    });

    describe('getMessages', () => {

    });

    describe('sendMessage', () => {

    });

    describe('emailIsUsed', () => {

    });

    describe('getBooks', () => {
        test('makes correct request', async () => {
            Client.authToken = 'myauthtoken';
            await Client.getBooks();
            expect(mock.history.get[0].headers.Authorization).toBe('Bearer myauthtoken');
        });

        test('returns book IDs as a list of strings', async () => {
            mock.onGet(Client.ROOT_PATH + '/book/get').replyOnce(200, [{bookid: '1234'}, {bookid: '2345'}, {bookid: '3456'}]);
            const books = await Client.getBooks();
            expect(books).toStrictEqual(['1234', '2345', '3456']);
        });

        test('returns empty list when there are no books', async () => {
            mock.onGet(Client.ROOT_PATH + '/book/get').replyOnce(200, []);
            const books = await Client.getBooks();
            expect(books).toStrictEqual([]);
        });

        test('returns null on failure', async () => {
            mock.onGet(Client.ROOT_PATH + '/book/get').replyOnce(400, []);
            const books = await Client.getBooks();
            expect(books).toBeNull();
        })
    });

    describe('addBooks', () => {

    });

    describe('removeBooks', () => {

    });
})