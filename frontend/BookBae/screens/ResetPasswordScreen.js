import React from 'react';
import {StyleSheet, Text, SafeAreaView} from 'react-native';

const ResetPasswordScreen = () => {
  return (
    <SafeAreaView style={styles.container}>
      <Text>Reset Your Password Here!</Text>
    </SafeAreaView>
  );
};

export default ResetPasswordScreen;

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
});
