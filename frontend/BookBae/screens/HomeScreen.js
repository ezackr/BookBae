import React from 'react';
import {StyleSheet, Text, SafeAreaView} from 'react-native';
import {createMaterialTopTabNavigator} from '@react-navigation/material-top-tabs';
import MainScreen from './MainScreen';
import MessageScreen from './MessageScreen';
import ProfileScreen from './ProfileScreen';

const Tab = createMaterialTopTabNavigator();

const HomeScreen = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Tab.Navigator>
        <Tab.Screen name="MainScreen" component={MainScreen} />
        <Tab.Screen name="MessageScreen" component={MessageScreen} />
        <Tab.Screen name="ProfileScreen" component={ProfileScreen} />
      </Tab.Navigator>
    </SafeAreaView>
  );
};

export default HomeScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
});
