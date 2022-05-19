import 'react-native';
import React from 'react';
import EnterPasswordScreen from '../screens/onboarding/EnterPasswordScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterPasswordScreen />);
})