import 'react-native';
import React from 'react';
import EnterGenderScreen from '../screens/onboarding/EnterGenderScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterGenderScreen />);
})