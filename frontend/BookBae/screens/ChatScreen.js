import React from 'react';
import {StyleSheet, Text, SafeAreaView} from 'react-native';

const ChatScreen = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Text>Chat</Text>
    </SafeAreaView>
  );
};

export default ChatScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
});
