import React from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';
import HomeScreen from './HomeScreen';
import ProfileScreen from './ProfileScreen';
import ChatScreen from './ChatScreen';

const Tab = createBottomTabNavigator();

const TabNavigation = () => {
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
};

export default TabNavigation;
