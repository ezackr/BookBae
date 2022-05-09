import React from 'react';
import {
  StyleSheet,
  Text,
  SafeAreaView,
  Pressable,
  TextInput,
} from 'react-native';

const LoginScreen = ({navigation}) => {
  const [email, onChangeEmail] = React.useState(null);
  const [password, onChangePassword] = React.useState(null);

  const handleLogin = async () => {
    navigation.navigate('HomeScreen');
  };

  const handleCreateAccount = () => {
    navigation.navigate('CreateAccountScreen');
  };

  const handleForgotPassword = () => {
    navigation.navigate('ResetPasswordScreen');
  };

  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title} numberOfLines={1}>
        Book Bae:
      </Text>
      <TextInput
        style={styles.input}
        numberOfLines={1}
        onChangeText={onChangeEmail}
        value={email}
        placeholder="Email"
        keyboardType="default"
      />
      <TextInput
        style={styles.input}
        numberOfLines={1}
        onChangeText={onChangePassword}
        value={password}
        placeholder="Password"
        keyboardType="default"
        secureTextEntry={true}
      />
      <Pressable style={styles.button} onPress={handleLogin}>
        <Text style={styles.buttonText}>Login</Text>
      </Pressable>
      <Pressable style={styles.button} onPress={handleForgotPassword}>
        <Text style={styles.buttonText}>Forgot Password</Text>
      </Pressable>
      <Pressable style={styles.button} onPress={handleCreateAccount}>
        <Text style={styles.buttonText}>Create Account</Text>
      </Pressable>
    </SafeAreaView>
  );
};

export default LoginScreen;

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
