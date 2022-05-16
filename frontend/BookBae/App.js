/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */
import React from 'react';
import {StyleSheet, View, Text} from 'react-native';

import ResetPasswordScreen from './screens/ResetPasswordScreen';
import LoginScreen from './screens/LoginScreen'
import Onboarder from './screens/onboarding/Onboarder'
import TabNavigation from './screens/TabNavigation'

import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
//incorporate icons later
//import Icon from 'react-native-ionicons';

const Stack = createStackNavigator();

const App = () => {
    return (
        <NavigationContainer>
            <Stack.Navigator>
                 <Stack.Screen name="LoginScreen" component={LoginScreen} />
                 <Stack.Screen
                    name="Onboarder"
                    component={Onboarder}
                    options={{headerShown: false}}
                 />
                 <Stack.Screen
                    name="TabNavigation"
                    component={TabNavigation}
                    options={{headerShown: false}}
                 />
                <Stack.Screen
                    name="ResetPasswordScreen"
                    component={ResetPasswordScreen}
                />
            </Stack.Navigator>
        </NavigationContainer>
    )
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
