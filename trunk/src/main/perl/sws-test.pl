#!/usr/bin/perl
#
# Script to talk to the web api of sendregning.no.
#
# Author: Petter Reinholdtsen
# License: GNU General Public License
#
# To use it, create a file ~/.swsconfig with this content:
#  $username = 'user@domain.no';
#  $password = 'secret';

use strict;
use warnings;

require LWP::UserAgent;
use HTTP::Request::Common qw(POST);
use LWP::ConnCache;
use Getopt::Std;

use vars qw($username $password);

#$username = "foo@bar.no";
#$passowrd = "secret";

if ( -f $ENV{HOME} . "/.swsconfig" ) {
    # Load $username and $password
    require $ENV{HOME} . "/.swsconfig" ;
}
unless ($username && $password) {
    print <<EOF;
error: Missing ~/.swsconfig or its content.
error: It is required to set username and password.
error: It should look something like this:
error:   \$username = 'foo\@bar.no'; \$password = 'secret';
EOF
    exit 1;
}

my $url = 'https://www.sendregning.no/sws/j_security_check';
my $refurl = 'https://www.sendregning.no/sws/jaas/login.jsp';
my $butlerurl = 'https://www.sendregning.no/sws/butler.do';

my %opts;
getopts("a:dnt:u:x:X:", \%opts) || usage();

my $istesting = $opts{n};

$username = $opts{u} || $username;

my $action = $opts{a} || "select";
my $type = $opts{t} || "invoice";

print "A: $action T: $type\n" if $opts{d};

my $ua = sws_login($username, $password);

my $success;
if ("select" eq $action && "constants" eq $type) {
    $success = sws_get($ua, $action, $type);
} else {
    my $xmlref;
    if ($opts{X}) {
        $xmlref = [ $opts{X}, "input.xml",
                    Content_Type => 'text/xml',
                    ];
    } elsif ($opts{x}) {
        $xmlref = [ undef, "input.xml",
                    Content_Type => 'text/xml',
                    Content => "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" .
                    $opts{x},
                    ];
    } else {
        $xmlref =
            [ undef, "input.xml",
              Content_Type => 'text/xml',
              Content =>
              "<?xml version=\"1.0\" encoding=\"UTF-8\"?><select>ALL</select>"
              ];

    }
    $success = sws_post($ua, $action, $type, $xmlref );
}

exit ($success ? 0 : 1);

sub usage {
    print <<EOF;
$0 [options]
  -n             enable test mode when talking to sendregning.no
  -d             enable debugging info
  -a action
  -t type
  -x xml-code
  -X xml-file
  -u username

The possible values for action and type as well the XML format is
documented by sendregning.no.
EOF
    exit 0;
}

sub sws_login {
    my ($username, $password) = @_;

    my $ua = LWP::UserAgent->new;
    $ua->timeout(10);
    $ua->env_proxy;
    $ua->agent("NUUG-fakturascripter/0.1 ");

    # Allow redirects for push URLs.  This is required to get the sws
    # operations working.
    push @{ $ua->requests_redirectable }, 'POST';

    # Enable keepalive to speed up the communication.
    $ua->conn_cache(LWP::ConnCache->new());

    $ua->cookie_jar({}); # Enable cookies to store the session ID

    # First get the login page
    my $response = $ua->get($refurl);
    print $response->as_string if $opts{d};

    # Next, log in
    my $req = POST $url, [ j_username => $username,
                           j_password => $password ];
    $req->header('Referer', $refurl);
    print $req->as_string if $opts{d};

    # Pass request to the user agent and get a response back
    $response = $ua->request($req);

    # The response is 302, not 200.  That is expected
    if (302 == $response->code || $response->is_success) {
        print $response->as_string if $opts{d};
    } else {
        print $response->status_line, "\n" if $opts{d};
        print "error: not success!\n";
    }

    return $ua;
}

sub is_testurl {
    my $url = shift;
    $url .= "&test=true" if ($istesting);
    return $url;
}

sub sws_get {
    my ($ua, $action, $type) = @_;
    my $testurl = "$butlerurl?action=select&type=constant";
    $testurl = is_testurl($testurl);
    my $response = $ua->get($testurl);
    if ($response->is_success) {
        print $response->as_string;
        return 1;
    } else {
        print $response->as_string if $opts{d};
        return 0;
    }
}

sub sws_post {
    my ($ua, $action, $type, $xmlref) = @_;
    # This code do not work yet!
    # This is the error:
    #    The request sent by the client was syntactically incorrect
    #    (Parameter xml can't be omitted).
    my $testurl = "$butlerurl?action=$action&type=$type";
    $testurl = is_testurl($testurl);
    my $req = POST $testurl, Content_Type => 'form-data',
                             Content => [ "xml" => $xmlref ] ;

    print $req->as_string if $opts{d};

    my $response = $ua->request($req);
    if ($response->is_success) {
        if ($response->header("Content-Type") =~ m%^application/pdf;.*%) {
            print STDERR "Got PDF, saving as output.pdf\n";
            open(PDF, ">", "output.pdf") || die "Unable to write to output.pdf";
            print PDF $response->content;
            close(PDF);
        } else {
            print "HTTP post returned success\n";
            print $response->as_string;
        }
        return 1;
    } else {
        print STDERR "HTTP post returned failure\n";
        print STDERR $response->status_line . "\n";
        return 0;
    }
}
