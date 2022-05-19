import 'react-native';
import React from 'react';
import EnterZipcodeScreen from '../screens/onboarding/EnterZipcodeScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterZipcodeScreen />);
})