import React from 'react';
import {StyleSheet, Text, SafeAreaView} from 'react-native';

const MainScreen = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Text>Welcome to the Main Menu!</Text>
    </SafeAreaView>
  );
};

export default MainScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
});
