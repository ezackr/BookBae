import React, { useState } from 'react';
import { SafeAreaView, StyleSheet, Text, TextInput, Button, Pressable } from 'react-native';
import DatePicker from 'react-native-date-picker';

const EnterBirthdayScreen = ({navigation}) => {

    const [date, setDate] = useState(new Date())
    const [open, setOpen] = useState(false)

    //add necessary function to store birthday
    const onPress = () => {
        console.log(date)
        navigation.navigate('EnterZipcodeScreen')
    }

    return (
        <SafeAreaView style={styles.container}>
            <Text style={styles.title}>Enter Your Birthday</Text>
            <Button title="Enter Date" onPress={() => setOpen(true)} />
            <DatePicker
                modal
                open={open}
                mode="date"
                date={date}
                onConfirm={(date) => {
                  setOpen(false)
                  setDate(date)
                }}
                onCancel={() => {
                  setOpen(false)
                }}
              />
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

export default EnterBirthdayScreen;