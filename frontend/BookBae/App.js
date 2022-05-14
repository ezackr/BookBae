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
import ProfileScreen from './screens/ProfileScreen';
import ChatScreen from './screens/ChatScreen';
import CreateAccountScreen from './screens/onboarding/CreateAccountScreen';
import EnterEmailScreen from './screens/onboarding/EnterEmailScreen';
import EnterPasswordScreen from './screens/onboarding/EnterPasswordScreen';
import EnterNameScreen from './screens/onboarding/EnterNameScreen';
import EnterGenderScreen from './screens/onboarding/EnterGenderScreen';
import EnterBirthdayScreen from './screens/onboarding/EnterBirthdayScreen';
import EnterZipcodeScreen from './screens/onboarding/EnterZipcodeScreen';
import EnterGenreScreen from './screens/onboarding/EnterGenreScreen';
import EnterBooksScreen from './screens/onboarding/EnterBooksScreen';
import EnterPhotoScreen from './screens/onboarding/EnterPhotoScreen';
import EnterBioScreen from './screens/onboarding/EnterBioScreen';
import ResetPasswordScreen from './screens/ResetPasswordScreen';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
//incorporate icons later
//import Icon from 'react-native-ionicons';

const Stack = createStackNavigator();

const Tab = createBottomTabNavigator();

const App = () => {
  return (
    <NavigationContainer>
      <Stack.Navigator>
        <Stack.Screen name="LoginScreen" component={LoginScreen} />
        <Stack.Screen
          name="TabNavigation"
          component={TabNavigation}
          options={{headerShown: false}}
        />
        <Stack.Screen
          name="CreateAccountScreen"
          component={CreateAccountScreen}
        />
        <Stack.Screen name="EnterEmailScreen" component={EnterEmailScreen} />
        <Stack.Screen
          name="EnterPasswordScreen"
          component={EnterPasswordScreen}
        />
        <Stack.Screen name="EnterNameScreen" component={EnterNameScreen} />
        <Stack.Screen name="EnterGenderScreen" component={EnterGenderScreen} />
        <Stack.Screen
          name="EnterBirthdayScreen"
          component={EnterBirthdayScreen}
        />
        <Stack.Screen
          name="EnterZipcodeScreen"
          component={EnterZipcodeScreen}
        />
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

function TabNavigation() {
  return (
    <Tab.Navigator>
      <Tab.Screen
        name="Home"
        component={HomeScreen}
        options={
          {
            //incorporate icon later
            //tabBarIcon: () => (<Icon name="information-circle-outline" color="blue" />),
          }
        }
      />
      <Tab.Screen
        name="Profile"
        component={ProfileScreen}
        options={
          {
            //incorporate icon later
            //tabBarIcon: () => <Icon name="person-circle-outline" size={40} color="blue"/>,
          }
        }
      />
      <Tab.Screen
        name="Chat"
        component={ChatScreen}
        options={
          {
            //incorporate icon later
            //tabBarIcon: () => <Icon name="person-circle-outline" size={40} color="blue"/>,
          }
        }
      />
    </Tab.Navigator>
  );
}

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
