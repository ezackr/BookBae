import 'react-native';
import React from 'react';
import EnterPhotoScreen from '../screens/onboarding/EnterPhotoScreen';
import renderer from 'react-test-renderer';

test('renders without error', () => {
    renderer.create(<EnterPhotoScreen />);
})