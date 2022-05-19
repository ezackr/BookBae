import 'react-native';
import React from 'react';
import EnterGenreScreen from '../screens/onboarding/EnterGenreScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterGenreScreen />);
})