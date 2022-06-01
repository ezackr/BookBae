import React from 'react';
import {StyleSheet, View, Text} from 'react-native';
import LoginScreen from '../LoginScreen';
import HomeScreen from '../HomeScreen';
import ProfileScreen from '../ProfileScreen';
import ChatScreen from '../ChatScreen';
import CreateAccountScreen from './CreateAccountScreen';
import EnterEmailScreen from './EnterEmailScreen';
import EnterPasswordScreen from './EnterPasswordScreen';
import EnterNameScreen from './EnterNameScreen';
import EnterGenderScreen from './EnterGenderScreen';
import EnterBirthdayScreen from './EnterBirthdayScreen';
import EnterZipcodeScreen from './EnterZipcodeScreen';
import EnterGenreScreen from './EnterGenreScreen';
import EnterBooksScreen from './EnterBooksScreen';
import EnterPhotoScreen from './EnterPhotoScreen';
import EnterBioScreen from './EnterBioScreen';
import ResetPasswordScreen from '../ResetPasswordScreen';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';

const Stack = createStackNavigator();

const Onboarder = () => {
    return (
        <Stack.Navigator>
            <Stack.Screen name="EnterEmailScreen" component={EnterEmailScreen} />
            <Stack.Screen name="EnterPasswordScreen" component={EnterPasswordScreen} />
            <Stack.Screen name="EnterNameScreen" component={EnterNameScreen} />
            <Stack.Screen name="EnterGenderScreen" component={EnterGenderScreen} />
            <Stack.Screen name="EnterBirthdayScreen" component={EnterBirthdayScreen} />
            <Stack.Screen name="EnterZipcodeScreen" component={EnterZipcodeScreen} />
            <Stack.Screen name="EnterGenreScreen" component={EnterGenreScreen} />
            <Stack.Screen name="EnterBooksScreen" component={EnterBooksScreen} />
            <Stack.Screen name="EnterPhotoScreen" component={EnterPhotoScreen} />
            <Stack.Screen name="EnterBioScreen" component={EnterBioScreen} />
            <Stack.Screen name="ResetPasswordScreen" component={ResetPasswordScreen} />
        </Stack.Navigator>
    );
}

export default Onboarder;