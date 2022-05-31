import React from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Button, Pressable } from 'react-native';
import Client from '../../Client'
import { passwordStrength } from 'check-password-strength'

const EnterPasswordScreen = ({route, navigation}) => {

    const [password, updatePassword] = React.useState(null);

    // example strength:
    //    "id": 1,
    //    "value": "Strong",
    //    "contains": ['lowercase', 'uppercase', 'symbol', 'number'],
    //    "length": 15
    // Possible values are "Too weak, Weak, Medium, & Strong.
    const [strength, updateStrength] = React.useState(passwordStrength(''));

    // Update value of text box (password) and check whether
    // password meets strength requirements
    const onChangeText = (newPassword) => {
        updatePassword(newPassword);
        updateStrength(passwordStrength(newPassword));
    }

    // Proceed to next screen iff password is not 'Too weak'
    const onPress = () => {
        if (strength.id > 0) {
            console.log(password)
            Client.createUser(route.params.email, password);
            navigation.navigate('EnterPhotoScreen', {
                ...route.params,
                password: password
            })
        }
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter Your Password</Text>
            <TextInput style={styles.input} multiline={true} onChangeText={onChangeText} value={password} placeholder="xxxxxxxx"/>
            <Text> {strength.value} </Text>
            <Pressable
                style={styles.button}
                onPress={onPress}>
                <Text style={styles.buttonText}>Next</Text>
             </Pressable>
        </SafeAreaView>
    );

}

const styles = StyleSheet.create({
  title: {
    color: 'black',
    fontSize: 24,
    fontWeight: 'bold',
  },
  input: {
    height: 40,
    width: '70%',
    margin: 1,
    borderWidth: 1,
    padding: 10,
  },
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
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

export default EnterPasswordScreen;
