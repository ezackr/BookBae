import 'react-native';
import React from 'react';
import EnterBioScreen from '../screens/onboarding/EnterBioScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterBioScreen />);
})