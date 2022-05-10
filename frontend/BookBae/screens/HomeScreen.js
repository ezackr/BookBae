import React from 'react';
import {StyleSheet, Text, SafeAreaView, Pressable} from 'react-native';

const HomeScreen = ({navigation}) => {
  return (
    <SafeAreaView style={styles.container}>
      <Text>Welcome to BookBae!</Text>
      <Pressable
        style={styles.button}
        onPress={navigation.navigate('MessagesScreen')}>
        <Text style={styles.buttonText}>Create Account</Text>
      </Pressable>
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
  title: {
    color: 'black',
    fontSize: 30,
    fontWeight: 'bold',
  },
  input: {
    height: 40,
    width: '70%',
    margin: 1,
    borderWidth: 1,
    padding: 10,
  },
  buttonText: {
    textAlign: 'center',
    color: 'black',
    fontSize: 12,
    padding: 5,
  },
});
