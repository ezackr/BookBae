import 'react-native';
import React from 'react';
import EnterNameScreen from '../screens/onboarding/EnterNameScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterNameScreen />);
})