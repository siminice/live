const React = require('react');
const ReactDOM = require('react-dom')
const client = require('./client');

const root = '/api';

class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {feds: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/api/leagues'}).done(response => {
			this.setState({feds: response.entity._embedded.feds});
		});
	}

	render() {
		return (
			<FedList feds={this.state.feds}/>
		)
	}
}

class FedList extends React.Component{
	render() {
		var feds = this.props.feds.map(fed =>
			<Fed key={fed._links.self.href} fed={fed}/>
		);
		return (
			<table>
				<tbody>
					<tr>
						<th>Name</th>
						<th>Script</th>
						<th>Folder</th>
					</tr>
					{feds}
				</tbody>
			</table>
		)
	}
}

class Fed extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.fed.name}</td>
				<td>{this.props.fed.script}</td>
				<td>{this.props.fed.dir}</td>
			</tr>
		)
	}
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)