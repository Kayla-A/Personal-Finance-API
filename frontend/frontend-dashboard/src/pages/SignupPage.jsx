import { useState } from "react"
import "../styles/SignupPage.css"

function SignupPage() {
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')

    function handleEmail(input) {
        setEmail(input.target.value)
    } // handleEmail

    function handlePassword(input) {
        setPassword(input.target.value)
    } // handlePassword

    function handleClick() {
        // send email and password to create user endpoint
        
    } // handleClick

    return (
        <>
            <title>Signup</title>
            <div className="auth-container">
                <form className="auth-form">
                    <h1>Sign up</h1>

                    <label>Enter email</label>
                    <input onChange={handleEmail} type="text" name="email" />

                    <label>Enter password</label>
                    <input onChange={handlePassword} type="text" name="password" />

                    <button onClick={handleClick}>Sign up</button>

                    <hr />

                    <p>Already have an account? <a href="https://www.google.com">Log in</a></p>
                </form>
            </div>
        </>
    ) // return 
} // SignupPage

export default SignupPage