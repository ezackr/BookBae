import Client from '../Client';
import axios from 'axios';
import AxiosMockAdapter from 'axios-mock-adapter'

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
})