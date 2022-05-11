/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 */
import React from 'react';
import {StyleSheet, View, Text} from 'react-native';
import LoginScreen from './screens/LoginScreen';
import HomeScreen from './screens/HomeScreen';
import CreateAccountScreen from './screens/CreateAccountScreen';
import EnterEmailScreen from './screens/EnterEmailScreen';
import EnterPasswordScreen from './screens/EnterPasswordScreen';
import EnterGenderScreen from './screens/EnterGenderScreen';
import EnterBirthdayScreen from './screens/EnterBirthdayScreen';
import EnterZipcodeScreen from './screens/EnterZipcodeScreen';
import EnterGenreScreen from './screens/EnterGenreScreen';
import EnterBooksScreen from './screens/EnterBooksScreen';
import EnterPhotoScreen from './screens/EnterPhotoScreen';
import EnterBioScreen from './screens/EnterBioScreen';
import ResetPasswordScreen from './screens/ResetPasswordScreen';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';

const Stack = createStackNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="LoginScreen" component={LoginScreen} />
        <Stack.Screen name="HomeScreen" component={HomeScreen} />
        <Stack.Screen
          name="CreateAccountScreen"
          component={CreateAccountScreen}
        />
        <Stack.Screen name="EnterEmailScreen" component={EnterEmailScreen} />
        <Stack.Screen name="EnterPasswordScreen" component={EnterPasswordScreen} />
        <Stack.Screen name="EnterGenderScreen" component={EnterGenderScreen} />
        <Stack.Screen name="EnterBirthdayScreen" component={EnterBirthdayScreen} />
        <Stack.Screen name="EnterZipcodeScreen" component={EnterZipcodeScreen} />
        <Stack.Screen name="EnterGenreScreen" component={EnterGenreScreen} />
        <Stack.Screen name="EnterBooksScreen" component={EnterBooksScreen} />
        <Stack.Screen name="EnterPhotoScreen" component={EnterPhotoScreen} />
        <Stack.Screen name="EnterBioScreen" component={EnterBioScreen} />
        <Stack.Screen
          name="ResetPasswordScreen"
          component={ResetPasswordScreen}
        />
      </Stack.Navigator>
    </NavigationContainer>
  );
};

export default App;
