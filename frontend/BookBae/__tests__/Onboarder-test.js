import 'react-native';
import React from 'react';
import Onboarder from '../screens/onboarding/Onboarder';
import renderer from 'react-test-renderer';
import {NavigationContainer} from '@react-navigation/native';

test('renders without error', () => {
    renderer.create(<NavigationContainer> <Onboarder /> </NavigationContainer>);
});