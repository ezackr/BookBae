import 'react-native';
import React from 'react';
import EnterBooksScreen from '../screens/onboarding/EnterBooksScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterBooksScreen />);
})