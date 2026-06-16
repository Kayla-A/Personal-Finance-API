import "../styles/SignupPage.css"

function SignupPage() {
    return (
        <>
            <title>Signup</title>
            <div className="auth-container">
                <form className="auth-form">
                    <h1>Sign up</h1>

                    <label>Enter email</label>
                    <input type="text" name="email" />

                    <label>Enter password</label>
                    <input type="text" name="password" />

                    <button>Sign up</button>

                    <hr />

                    <p>Already have an account? <a href="https://www.google.com">Log in</a></p>
                </form>
            </div>
        </>
    ) // return 
} // SignupPage

export default SignupPage