import React from 'react';
import {
  StyleSheet,
  Text,
  SafeAreaView,
  Pressable,
  TextInput,
  Button,
} from 'react-native';

const LoginScreen = ({navigation}) => {
  const [email, onChangeEmail] = React.useState(null);
  const [password, onChangePassword] = React.useState(null);

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
      <Pressable style={styles.button}
        onPress={() => navigation.navigate('HomeScreen')}>
        <Text style={styles.buttonText}>Login</Text>
      </Pressable>
      <Pressable style={styles.button}
        onPress={() => console.log('Forgot Password!')}>
        <Text style={styles.buttonText}>Forgot Password</Text>
      </Pressable>
      <Pressable
        style={styles.button}
        onPress={() => navigation.navigate('EnterEmailScreen')}>
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
  button: {
    alignItems: 'center',
    justifyContent: 'center',
    paddingVertical: 12,
    paddingHorizontal: 32,
    borderRadius: 4,
    elevation: 3,
    backgroundColor: '#BD2A2A',
    marginTop: 5,
    marginBottom: 5,
  },
  buttonText: {
    fontSize: 16,
    lineHeight: 21,
    fontWeight: 'bold',
    letterSpacing: 0.25,
    color: 'white',
  },
});
