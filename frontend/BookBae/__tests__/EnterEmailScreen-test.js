import 'react-native';
import React from 'react';
import EnterEmailScreen from '../screens/onboarding/EnterEmailScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterEmailScreen />);
})