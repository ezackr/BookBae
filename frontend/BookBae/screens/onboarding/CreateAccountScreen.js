import React from 'react';
import {StyleSheet, Text, SafeAreaView} from 'react-native';

const CreateAccountScreen = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Text>Create Your First Account!</Text>
    </SafeAreaView>
  );
};

export default CreateAccountScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
});