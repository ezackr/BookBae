import React, {useState} from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Button, Pressable } from 'react-native';
import RadioForm from 'react-native-simple-radio-button';

const EnterGenderScreen = ({route, navigation}) => {

    const genders = [{ label: 'female', value: 'f'}, {label: 'male', value: 'm'}, {label: 'non-binary', value: 'n'}]

    const [option, setOption] = useState('female');

    //add necessary function to store gender
    const onPress = () => {
        console.log(option)
        navigation.navigate('EnterBirthdayScreen', {
            email: route.params.email,
            password: route.params.password,
            gender: option
        })
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter Your Gender</Text>
            <RadioForm radio_props={genders} onPress={(value) => {setOption(value)}}/>
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
  container: {
    flex: 1,
    backgroundColor: '#fffdd3',
    //alignItems: 'center',
    justifyContent: 'center',
    fontWeight: 'bold',
  },
  input: {
      height: 40,
      margin: 12,
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

export default EnterGenderScreen;