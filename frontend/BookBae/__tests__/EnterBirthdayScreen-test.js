import 'react-native';
import React from 'react';
import EnterBirthdayScreen from '../screens/onboarding/EnterBirthdayScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterBirthdayScreen />);
})