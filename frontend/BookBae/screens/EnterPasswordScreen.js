import React from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Button, Pressable } from 'react-native';

const EnterPasswordScreen = ({navigation}) => {

    const [password, onChangeText] = React.useState(null);

    //add necessary function to store password
    const onPress = () => {
        console.log(password)
        navigation.navigate('EnterGenderScreen')
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter Your Password</Text>
            <TextInput style={styles.input} multiline={true} onChangeText={onChangeText} value={password} placeholder="xxxxxxxx"/>
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