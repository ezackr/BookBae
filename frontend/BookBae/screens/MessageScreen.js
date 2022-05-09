import React from 'react';
import {StyleSheet, Text, SafeAreaView} from 'react-native';

const MessageScreen = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Text>Send a Message!</Text>
    </SafeAreaView>
  );
};

export default MessageScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
});
